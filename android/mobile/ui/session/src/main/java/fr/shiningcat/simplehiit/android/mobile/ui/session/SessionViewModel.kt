package fr.shiningcat.simplehiit.android.mobile.ui.session

import android.media.SoundPool
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.TimeProvider
import fr.shiningcat.simplehiit.commonutils.di.MainDispatcher
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.Session
import fr.shiningcat.simplehiit.domain.common.models.SessionRecord
import fr.shiningcat.simplehiit.domain.common.models.SessionStep
import fr.shiningcat.simplehiit.domain.common.models.SessionStepDisplay
import fr.shiningcat.simplehiit.domain.common.models.StepTimerState
import fr.shiningcat.simplehiit.domain.common.usecases.DurationFormatStyle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel
    @Inject
    constructor(
        private val sessionInteractor: SessionInteractor,
        private val mapper: SessionViewStateMapper,
        @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
        private val timeProvider: TimeProvider,
        private val soundPool: SoundPool,
        private val hiitLogger: HiitLogger,
    ) : ViewModel() {
        // Screen state - manually managed due to complex ViewModel-internal state dependencies
        // (session object, currentSessionStepIndex) that can't be purely derived from flows
        private val _screenViewState =
            MutableStateFlow<SessionViewState>(SessionViewState.Loading)
        val screenViewState = _screenViewState.asStateFlow()

        // UI-driven state - managed manually for dialogs and one-time events
        private val _dialogViewState = MutableStateFlow<SessionDialog>(SessionDialog.None)
        val dialogViewState = _dialogViewState.asStateFlow()

        // Timer state - reactive data stream from interactor
        // Note: We don't use stateIn here because we need the raw StateFlow from the use case
        // to maintain timer continuity. The timer must keep running even when UI is not observing.
        private val timerStateFlow = sessionInteractor.getStepTimerState()

        // ViewModel-internal state for session management
        private var session: Session? = null
        private var currentSessionStepIndex = 0
        private var stepTimerJob: Job? = null
        private var beepSoundLoadedId: Int? = null

        init {
            hiitLogger.d("SessionViewModel", "initializing with hybrid state management")
            hiitLogger.d(
                "SessionViewModel",
                "soundPool created, awaiting sound to be loaded to proceed",
            )
            soundPool.setOnLoadCompleteListener { _: SoundPool, sampleId: Int, loadingStatus: Int ->
                // loading success if status == 0
                if (loadingStatus == 0) {
                    this.beepSoundLoadedId = sampleId
                    hiitLogger.d(
                        "SessionViewModel",
                        "sound loaded in soundPool, proceeding with SessionViewModel initialization...",
                    )
                } else {
                    hiitLogger.e(
                        "SessionViewModel",
                        "SOUND FAILED LOADING IN SOUNDPOOL, proceeding with SessionViewModel initialization...",
                    )
                }
                setupTicker()
                retrieveSettingsAndProceed()
            }
        }

        fun getSoundPool(): SoundPool? = soundPool

        fun isSoundLoaded() = beepSoundLoadedId != null

        private fun setupTicker() {
            viewModelScope.launch(context = mainDispatcher) {
                timerStateFlow.collect { stepTimerState ->
                    if (stepTimerState != StepTimerState()) { // excluding first emission with default value
                        tick(stepTimerState)
                    }
                }
            }
        }

        private fun retrieveSettingsAndProceed() {
            viewModelScope.launch(context = mainDispatcher) {
                sessionInteractor.getSessionSettings().collect { sessionSettingsOutput ->
                    when (sessionSettingsOutput) {
                        is Output.Error -> {
                            hiitLogger.e(
                                "SessionViewModel",
                                "init::getSessionSettingsUseCase returned error: ",
                                sessionSettingsOutput.exception,
                            )
                            _screenViewState.emit(
                                SessionViewState.Error(
                                    sessionSettingsOutput.errorCode.code,
                                ),
                            )
                        }

                        is Output.Success -> {
                            val sessionSettingsResult = sessionSettingsOutput.result
                            session =
                                sessionInteractor.buildSession(
                                    sessionSettings = sessionSettingsResult,
                                )
                            launchSession()
                        }
                    }
                }
            }
        }

        private fun launchSession() {
            val immutableSession = session
            if (immutableSession == null) {
                hiitLogger.e("SessionViewModel", "tick::session is NULL!")
                viewModelScope.launch(context = mainDispatcher) {
                    _screenViewState.emit(SessionViewState.Error(Constants.Errors.SESSION_NOT_FOUND.code))
                }
            } else {
                stepTimerJob?.cancel()
                val wholeSessionDuration = immutableSession.durationMs
                stepTimerJob =
                    viewModelScope.launch {
                        sessionInteractor.startStepTimer(totalMilliSeconds = wholeSessionDuration)
                    }
            }
        }

        private fun tick(stepTimerState: StepTimerState) {
            val immutableSession = session
            if (immutableSession == null) {
                hiitLogger.e("SessionViewModel", "tick::session is NULL!")
                viewModelScope.launch(context = mainDispatcher) {
                    _screenViewState.emit(SessionViewState.Error(Constants.Errors.SESSION_NOT_FOUND.code))
                }
            } else {
                val currentStep = immutableSession.steps[currentSessionStepIndex]
                val sessionRemainingMs = stepTimerState.milliSecondsRemaining
                hiitLogger.d(
                    tag = "SessionViewModel",
                    msg =
                        "tick: step $currentStep: remaining ms: $sessionRemainingMs",
                )
                if (sessionRemainingMs == 0L) { // whole session end
                    // play last (when timer reaches 0) beep sound
                    maybePlayBeepSound(forceBeep = immutableSession.beepSoundCountDownActive)
                    hiitLogger.d("SessionViewModel", "tick: Session finished")
                    emitSessionEndState()
                } else { // build current running step state and emit
                    val timeRemainingTriggerNextStep = currentStep.remainingSessionDurationMsAfterMe
                    if (sessionRemainingMs <= timeRemainingTriggerNextStep) {
                        // play step's last beep sound as the increase of currentSessionStepIndex will prevent the state with a 0 countDown to be emitted
                        // we do not display the "0" remaining seconds, but we want the beep sound to be played if setting is on
                        maybePlayBeepSound(forceBeep = immutableSession.beepSoundCountDownActive)
                        currentSessionStepIndex += 1
                        hiitLogger.d(
                            tag = "SessionViewModel",
                            msg =
                                "tick: step $currentStep has ended, incrementing currentSessionStepIndex to:" +
                                    " $currentSessionStepIndex / ${immutableSession.steps.size - 1}",
                        )
                    }
                    viewModelScope.launch {
                        val currentState =
                            mapper.buildStateFromWholeSession(
                                session = immutableSession,
                                currentSessionStepIndex = currentSessionStepIndex,
                                currentStepTimerState = stepTimerState,
                            )
                        maybePlayBeepSound(currentState = currentState)
                        _screenViewState.emit(currentState)
                    }
                }
            }
        }

        private fun maybePlayBeepSound(
            currentState: SessionViewState? = null,
            forceBeep: Boolean? = false,
        ) {
            if (forceBeep == true) {
                playBeepSound()
                return
            }
            when (currentState) {
                is SessionViewState.InitialCountDownSession -> if (currentState.countDown.playBeep) playBeepSound()
                is SessionViewState.RunningNominal -> if (currentState.countDown?.playBeep == true) playBeepSound()
                else -> {} // do nothing
            }
        }

        private fun playBeepSound() {
            val loadedSound = beepSoundLoadedId
            if (loadedSound == null) {
                hiitLogger.e(
                    "SessionViewModel",
                    "playBeepSound::no sound loaded in SoundPool! Has the sound been loaded through SessionViewModel.setLoadedSound?",
                )
                return
            }
            soundPool.play(loadedSound, 1f, 1f, 0, 0, 1f)
        }

        private fun emitSessionEndState() {
            viewModelScope.launch(context = mainDispatcher) {
                val immutableSession = session
                if (immutableSession == null) {
                    hiitLogger.e("SessionViewModel", "emitSessionEndState::session is NULL!")
                    _screenViewState.emit(SessionViewState.Error(Constants.Errors.SESSION_NOT_FOUND.code))
                } else {
                    if (immutableSession.steps.last() is SessionStep.RestStep) {
                        // not counting the last Rest step for aborted session as it doesn't make much sense:
                        currentSessionStepIndex -= 1
                    }
                    val restStepsDone =
                        immutableSession.steps
                            .take(currentSessionStepIndex + 1) // we want to include the last step
                            .filterIsInstance<SessionStep.RestStep>()
                    val workingStepsDone =
                        immutableSession.steps
                            .take(currentSessionStepIndex + 1) // we want to include the last step
                            .filterIsInstance<SessionStep.WorkStep>()
                    val actualSessionLength =
                        if (restStepsDone.isNotEmpty() && workingStepsDone.isNotEmpty()) {
                            restStepsDone.size.times(restStepsDone[0].durationMs).plus(
                                workingStepsDone.size.times(workingStepsDone[0].durationMs),
                            )
                        } else {
                            0L
                        }
                    hiitLogger.d(
                        tag = "SessionViewModel",
                        msg =
                            "emitSessionEndState::workingStepsDone = ${workingStepsDone.size} " +
                                "| restStepsDone = ${restStepsDone.size} " +
                                "| total steps = ${workingStepsDone.size + restStepsDone.size}",
                    )
                    hiitLogger.d(
                        tag = "SessionViewModel",
                        msg = "emitSessionEndState::actualSessionLength = $actualSessionLength",
                    )
                    val actualSessionLengthFormatted =
                        sessionInteractor.formatLongDurationMsAsSmallestHhMmSsString(
                            durationMs = actualSessionLength,
                            formatStyle = DurationFormatStyle.SHORT,
                        )
                    val workingStepsDoneDisplay =
                        workingStepsDone.map {
                            SessionStepDisplay(
                                exercise = it.exercise,
                                side = it.side,
                            )
                        }
                    // record session done if it's not empty
                    if (actualSessionLength > 0L) {
                        val sessionRecord =
                            SessionRecord(
                                timeStamp = timeProvider.getCurrentTimeMillis(),
                                durationMs = actualSessionLength,
                                usersIds = session?.users?.map { it.id } ?: emptyList(),
                            )
                        hiitLogger.d(
                            "SessionViewModel",
                            "emitSessionEndState::sessionRecord: $sessionRecord",
                        )
                        sessionInteractor.insertSession(sessionRecord)
                    }
                    hiitLogger.d(
                        "SessionViewModel",
                        "emitSessionEndState emitting finished state: $actualSessionLengthFormatted",
                    )
                    _screenViewState.emit(
                        SessionViewState.Finished(
                            sessionDurationFormatted = actualSessionLengthFormatted,
                            workingStepsDone = workingStepsDoneDisplay,
                        ),
                    )
                }
            }
        }

        fun pause() {
            hiitLogger.d("SessionViewModel", "pause")
            val immutableSession = session
            if (immutableSession == null) {
                viewModelScope.launch(context = mainDispatcher) {
                    _screenViewState.emit(SessionViewState.Error(Constants.Errors.SESSION_NOT_FOUND.code))
                }
            } else {
                hiitLogger.d("SessionViewModel", "pause::stopping stepTimer")
                stepTimerJob?.cancel()
                val currentStep = immutableSession.steps[currentSessionStepIndex]
                if (currentStep is SessionStep.WorkStep) {
                    currentSessionStepIndex -= 1 // safe as the first step will always be a REST
                }
                viewModelScope.launch(context = mainDispatcher) {
                    _dialogViewState.emit(SessionDialog.Pause)
                }
            }
        }

        fun resume() {
            val immutableSession = session
            if (immutableSession == null) {
                viewModelScope.launch(context = mainDispatcher) {
                    _screenViewState.emit(SessionViewState.Error(Constants.Errors.SESSION_NOT_FOUND.code))
                }
                return
            }
            val stepToStart = immutableSession.steps[currentSessionStepIndex]
            val remainingTotalTimeToLaunch =
                stepToStart.durationMs.plus(stepToStart.remainingSessionDurationMsAfterMe)
            stepTimerJob =
                viewModelScope.launch(context = mainDispatcher) {
                    sessionInteractor.startStepTimer(totalMilliSeconds = remainingTotalTimeToLaunch)
                }
            viewModelScope.launch(context = mainDispatcher) {
                _dialogViewState.emit(SessionDialog.None)
            }
        }

        fun abortSession() {
            hiitLogger.d("SessionViewModel", "abortSession")
            viewModelScope.launch(context = mainDispatcher) {
                emitSessionEndState()
                _dialogViewState.emit(SessionDialog.None)
            }
        }

        override fun onCleared() {
            super.onCleared()
            hiitLogger.d("SessionViewModel", "onCleared::cancelling stepTimerJob")
            stepTimerJob?.cancel()
            hiitLogger.d("SessionViewModel", "onCleared::releasing SoundPool")
            soundPool.release()
        }
    }
