package fr.shining_cat.simplehiit.ui.session

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.di.MainDispatcher
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.*
import fr.shining_cat.simplehiit.domain.usecases.*
import fr.shining_cat.simplehiit.ui.AbstractLoggerViewModel
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val mapper: SessionMapper,
    private val getSessionSettingsUseCase: GetSessionSettingsUseCase,
    private val buildSessionUseCase: BuildSessionUseCase,
    private val formatLongDurationMsAsSmallestHhMmSsStringUseCase: FormatLongDurationMsAsSmallestHhMmSsStringUseCase,
    private val stepTimerUseCase: StepTimerUseCase,
    private val insertSessionUseCase: InsertSessionUseCase,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val hiitLogger: HiitLogger

) : AbstractLoggerViewModel(hiitLogger) {

    private val _screenViewState =
        MutableStateFlow<SessionViewState>(SessionViewState.Loading)
    val screenViewState = _screenViewState.asStateFlow()
    private val _dialogViewState = MutableStateFlow<SessionDialog>(SessionDialog.None)
    val dialogViewState = _dialogViewState.asStateFlow()

    //
    private var isInitialized = false
    private var durationStringFormatter = DurationStringFormatter()
    private var session: Session? = null
    private var currentSessionStepIndex = 0
    private var stepTimerJob: Job? = null

    //
    fun init(durationStringFormatter: DurationStringFormatter) {
        if (!isInitialized) {
            this.durationStringFormatter = durationStringFormatter
            //
            setupTicker()
            //
            retrieveSettingsAndProceed()
            //
            isInitialized = true
        }
    }

    private fun setupTicker() {
        viewModelScope.launch(context = mainDispatcher) {
            stepTimerUseCase.timerStateFlow.collect() { stepTimerState ->
                if (stepTimerState != StepTimerState()) { //excluding first emission with default value
                    tickWhole(stepTimerState)
                }
            }
        }
    }

    private fun retrieveSettingsAndProceed() {
        viewModelScope.launch(context = mainDispatcher) {
            getSessionSettingsUseCase.execute().collect() { sessionSettingsOutput ->
                when (sessionSettingsOutput) {
                    is Output.Error -> {
                        hiitLogger.e(
                            "SessionViewModel",
                            "init::getSessionSettingsUseCase returned error: ",
                            sessionSettingsOutput.exception
                        )
                        _screenViewState.emit(
                            SessionViewState.Error(
                                sessionSettingsOutput.errorCode.code
                            )
                        )
                    }
                    is Output.Success -> {
                        val sessionSettingsResult = sessionSettingsOutput.result
                        session = buildSessionUseCase.execute(
                            sessionSettings = sessionSettingsResult,
                            durationStringFormatter = durationStringFormatter
                        )
                        launchWholeSession()
                    }
                }
            }
        }
    }

    private fun launchWholeSession() {
        val immutableSession = session
        if (immutableSession == null) {
            hiitLogger.e("SessionViewModel", "tick::session is NULL!")
            viewModelScope.launch(context = mainDispatcher) {
                _screenViewState.emit(SessionViewState.Error(Constants.Errors.SESSION_NOT_FOUND.code))
            }
        } else {
            val wholeSessionDuration = immutableSession.durationMs
            stepTimerJob = viewModelScope.launch {
                stepTimerUseCase.start(totalMilliSeconds = wholeSessionDuration)
            }
        }
    }

    private fun tickWhole(stepTimerState: StepTimerState) {
        val immutableSession = session
        if (immutableSession == null) {
            hiitLogger.e("SessionViewModel", "tick::session is NULL!")
            viewModelScope.launch(context = mainDispatcher) {
                _screenViewState.emit(SessionViewState.Error(Constants.Errors.SESSION_NOT_FOUND.code))
            }
        } else {
            val currentStep = immutableSession.steps[currentSessionStepIndex]
            val sessionRemainingSeconds = stepTimerState.milliSecondsRemaining
            if (sessionRemainingSeconds == 0L) {//whole session end
                if (immutableSession.steps.lastOrNull() == currentStep) {
                    hiitLogger.d(
                        "TimerSateViewModel",
                        "tickWhole: SESSION FINISHED, current step is LAST"
                    )
                    emitSessionEndState()
                } else {
                    hiitLogger.e(
                        "TimerSateViewModel",
                        "tickWhole: SESSION FINISHED, current step is NOT LAST"
                    )
                }
            } else {//build current running step state and emit
                val timeRemainingTriggerNextStep = currentStep.remainingSessionDurationMsAfterMe
                if (sessionRemainingSeconds <= timeRemainingTriggerNextStep) {
                    hiitLogger.d(
                        "TimerSateViewModel",
                        "tickWhole: step $currentStep has ended, incrementing currentSessionStepIndex"
                    )
                    currentSessionStepIndex += 1
                }
                viewModelScope.launch {
                    val currentState = mapper.buildStateWholeSession(
                        session = immutableSession,
                        currentSessionStepIndex = currentSessionStepIndex,
                        currentState = stepTimerState,
                        durationStringFormatter = durationStringFormatter
                    )
                    _screenViewState.emit(currentState)
                }
            }
        }
    }

    private fun emitSessionEndState() {
        viewModelScope.launch(context = mainDispatcher) {
            val immutableSession = session
            if (immutableSession == null) {
                hiitLogger.e("SessionViewModel", "emitSessionEndState::session is NULL!")
                _screenViewState.emit(SessionViewState.Error(Constants.Errors.SESSION_NOT_FOUND.code))
            } else {
                if (immutableSession.steps.last() is SessionStep.RestStep) {
                    //not counting the last Rest step for aborted session as it doesn't make much sense:
                    currentSessionStepIndex -= 1
                }
                val restStepsDone = immutableSession.steps
                    .take(currentSessionStepIndex + 1) // we want to include the last step
                    .filterIsInstance<SessionStep.RestStep>()
                val workingStepsDone = immutableSession.steps
                    .take(currentSessionStepIndex + 1) // we want to include the last step
                    .filterIsInstance<SessionStep.WorkStep>()
                val actualSessionLength =
                    if (restStepsDone.isNotEmpty() && workingStepsDone.isNotEmpty()) {
                        restStepsDone.size.times(restStepsDone[0].durationMs).plus(
                            workingStepsDone.size.times(workingStepsDone[0].durationMs)
                        )
                    } else 0L
                hiitLogger.d(
                    "SessionViewModel",
                    "emitSessionEndState::workingStepsDone = ${workingStepsDone.size} | restStepsDone = ${restStepsDone.size} | total steps = ${workingStepsDone.size + restStepsDone.size}"
                )
                hiitLogger.d(
                    "SessionViewModel",
                    "emitSessionEndState::actualSessionLength = $actualSessionLength"
                )
                val actualSessionLengthFormatted =
                    formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                        actualSessionLength, durationStringFormatter
                    )
                val workingStepsDoneDisplay = workingStepsDone.map {
                    SessionStepDisplay(
                        exercise = it.exercise,
                        side = it.side,
                    )
                }
                //record session done
                val sessionRecord = SessionRecord(
                    timeStamp = System.currentTimeMillis(),
                    durationMs = actualSessionLength,
                    usersIds = session?.users?.map { it.id } ?: emptyList()
                )
                hiitLogger.d("SessionViewModel", "sessionRecord: $sessionRecord")
                insertSessionUseCase.execute(sessionRecord)
                _screenViewState.emit(
                    SessionViewState.Finished(
                        sessionDurationFormatted = actualSessionLengthFormatted,
                        workingStepsDone = workingStepsDoneDisplay
                    )
                )
            }
        }
    }

    fun pause() {
        hiitLogger.d("SessionViewModel", "pause")
        viewModelScope.launch(context = mainDispatcher) {
            val immutableSession = session
            if (immutableSession == null) {
                _screenViewState.emit(SessionViewState.Error(Constants.Errors.SESSION_NOT_FOUND.code))
            } else {
                hiitLogger.d("SessionViewModel", "pause::stopping stepTimer")
                stepTimerJob?.cancel()
                val currentStep = immutableSession.steps[currentSessionStepIndex]
                if (currentStep is SessionStep.WorkStep) {
                    currentSessionStepIndex -= 1 //safe as the first step will always be a REST
                }
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
        val remainingTotalTimeToLaunch = stepToStart.durationMs.plus(stepToStart.remainingSessionDurationMsAfterMe)
        stepTimerJob = viewModelScope.launch(context = mainDispatcher) {
            stepTimerUseCase.start(totalMilliSeconds = remainingTotalTimeToLaunch)
        }
        viewModelScope.launch(context = mainDispatcher) {
            _dialogViewState.emit(SessionDialog.None)
        }
    }

    fun abortSession() {
//        hiitLogger.d("SessionViewModel", "abortSession")
        viewModelScope.launch(context = mainDispatcher) {
            emitSessionEndState()
            _dialogViewState.emit(SessionDialog.None)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stepTimerJob?.cancel()
    }

}