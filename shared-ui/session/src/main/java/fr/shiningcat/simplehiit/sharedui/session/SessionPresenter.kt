package fr.shiningcat.simplehiit.sharedui.session

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.TimeProvider
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.Session
import fr.shiningcat.simplehiit.domain.common.models.SessionRecord
import fr.shiningcat.simplehiit.domain.common.models.SessionSettings
import fr.shiningcat.simplehiit.domain.common.models.SessionStep
import fr.shiningcat.simplehiit.domain.common.models.SessionStepDisplay
import fr.shiningcat.simplehiit.domain.common.models.StepTimerState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Pure Kotlin presenter for session screen business logic.
 * Coordinates session initialization, timer management, and state transitions.
 * Emits beep signals for ViewModel to play via SoundPool.
 */
class SessionPresenter(
    private val sessionInteractor: SessionInteractor,
    private val mapper: SessionViewStateMapper,
    private val timeProvider: TimeProvider,
    private val dispatcher: CoroutineDispatcher,
    private val logger: HiitLogger,
) {
    // Coroutine scope for presenter operations - using SupervisorJob for proper cancellation
    private val presenterJob = SupervisorJob()
    private val presenterScope = CoroutineScope(presenterJob + dispatcher)
    private var tickerCollectionJob: Job? = null

    // Screen state - manually managed due to complex internal state dependencies
    private val _screenViewState = MutableStateFlow<SessionViewState>(SessionViewState.Loading)
    val screenViewState: StateFlow<SessionViewState> = _screenViewState.asStateFlow()

    // Dialog state - managed manually for pause dialog
    private val _dialogViewState = MutableStateFlow<SessionDialog>(SessionDialog.None)
    val dialogViewState: StateFlow<SessionDialog> = _dialogViewState.asStateFlow()

    // Beep signal - emits when ViewModel should play beep sound
    private val _beepSignal = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val beepSignal: SharedFlow<Unit> = _beepSignal.asSharedFlow()

    // Timer state from interactor - reactive data stream
    private val timerStateFlow = sessionInteractor.getStepTimerState()

    // Internal state for session management
    private var session: Session? = null
    private var currentSessionStepIndex = 0
    private var stepTimerJob: Job? = null
    private var soundLoaded = false

    /**
     * Called by ViewModel when SoundPool has loaded the beep sound.
     * Triggers session initialization flow.
     */
    fun onSoundLoaded() {
        logger.d("SessionPresenter", "onSoundLoaded - proceeding with initialization")
        soundLoaded = true
        initializeAndStartSession()
    }

    private fun initializeAndStartSession() {
        setupTicker()
        retrieveSettingsAndProceed()
    }

    private fun setupTicker() {
        tickerCollectionJob?.cancel()
        tickerCollectionJob =
            presenterScope.launch {
                timerStateFlow.collect { stepTimerState ->
                    if (stepTimerState != StepTimerState()) { // excluding first emission with default value
                        tick(stepTimerState)
                    }
                }
            }
    }

    private fun retrieveSettingsAndProceed() {
        presenterScope.launch {
            // Use first() instead of collect() to only take the initial settings
            // and prevent the session from being rebuilt when settings/users flows re-emit
            val sessionSettingsOutput = sessionInteractor.getSessionSettings().first()
            when (sessionSettingsOutput) {
                is Output.Error -> {
                    logger.e(
                        "SessionPresenter",
                        "retrieveSettingsAndProceed::getSessionSettings returned error: ",
                        sessionSettingsOutput.exception,
                    )
                    _screenViewState.emit(
                        SessionViewState.Error(
                            sessionSettingsOutput.errorCode.code,
                        ),
                    )
                }
                is Output.Success<*> -> {
                    val sessionSettingsResult = sessionSettingsOutput.result as SessionSettings
                    session =
                        sessionInteractor.buildSession(
                            sessionSettings = sessionSettingsResult,
                        )
                    launchSession()
                }
            }
        }
    }

    private fun launchSession() {
        val immutableSession = session
        if (immutableSession == null) {
            logger.e("SessionPresenter", "launchSession::session is NULL!")
            presenterScope.launch {
                _screenViewState.emit(SessionViewState.Error(DomainError.SESSION_NOT_FOUND.code))
            }
        } else {
            stepTimerJob?.cancel()
            val wholeSessionDuration = immutableSession.durationMs
            stepTimerJob =
                presenterScope.launch {
                    sessionInteractor.startStepTimer(totalMilliSeconds = wholeSessionDuration)
                }
        }
    }

    private suspend fun tick(stepTimerState: StepTimerState) {
        val immutableSession = session
        if (immutableSession == null) {
            logger.e("SessionPresenter", "tick::session is NULL!")
            _screenViewState.emit(SessionViewState.Error(DomainError.SESSION_NOT_FOUND.code))
        } else {
            val currentStep = immutableSession.steps[currentSessionStepIndex]
            val sessionRemainingMs = stepTimerState.milliSecondsRemaining
            logger.d(
                tag = "SessionPresenter",
                msg = "tick: step $currentStep: remaining ms: $sessionRemainingMs",
            )
            if (sessionRemainingMs == 0L) { // whole session end
                // play last (when timer reaches 0) beep sound
                maybePlayBeepSound(forceBeep = immutableSession.beepSoundCountDownActive)
                logger.d("SessionPresenter", "tick: Session finished")
                emitSessionEndState()
            } else { // build current running step state and emit
                val timeRemainingTriggerNextStep = currentStep.remainingSessionDurationMsAfterMe
                if (sessionRemainingMs <= timeRemainingTriggerNextStep) {
                    // play step's last beep sound as the increase of currentSessionStepIndex will prevent the state with a 0 countDown to be emitted
                    // we do not display the "0" remaining seconds, but we want the beep sound to be played if setting is on
                    maybePlayBeepSound(forceBeep = immutableSession.beepSoundCountDownActive)
                    currentSessionStepIndex += 1
                    logger.d(
                        tag = "SessionPresenter",
                        msg =
                            "tick: step $currentStep has ended, incrementing currentSessionStepIndex to:" +
                                " $currentSessionStepIndex / ${immutableSession.steps.size - 1}",
                    )
                }
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

    private fun maybePlayBeepSound(
        currentState: SessionViewState? = null,
        forceBeep: Boolean? = false,
    ) {
        if (forceBeep == true) {
            emitBeepSignal()
            return
        }
        when (currentState) {
            is SessionViewState.InitialCountDownSession -> {
                if (currentState.countDown.playBeep) emitBeepSignal()
            }
            is SessionViewState.RunningNominal -> {
                if (currentState.countDown?.playBeep == true) emitBeepSignal()
            }
            else -> {} // do nothing
        }
    }

    private fun emitBeepSignal() {
        _beepSignal.tryEmit(Unit)
    }

    private suspend fun emitSessionEndState() {
        val immutableSession = session
        if (immutableSession == null) {
            logger.e("SessionPresenter", "emitSessionEndState::session is NULL!")
            _screenViewState.emit(SessionViewState.Error(DomainError.SESSION_NOT_FOUND.code))
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
            logger.d(
                tag = "SessionPresenter",
                msg =
                    "emitSessionEndState::workingStepsDone = ${workingStepsDone.size} " +
                        "| restStepsDone = ${restStepsDone.size} " +
                        "| total steps = ${workingStepsDone.size + restStepsDone.size}",
            )
            logger.d(
                tag = "SessionPresenter",
                msg = "emitSessionEndState::actualSessionLength = $actualSessionLength",
            )
            val actualSessionLengthFormatted =
                sessionInteractor.formatLongDurationMsAsSmallestHhMmSsString(
                    durationMs = actualSessionLength,
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
                logger.d(
                    "SessionPresenter",
                    "emitSessionEndState::sessionRecord: $sessionRecord",
                )
                sessionInteractor.insertSession(sessionRecord)
            }
            logger.d(
                "SessionPresenter",
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

    fun pause() {
        logger.d("SessionPresenter", "pause")
        val immutableSession = session
        if (immutableSession == null) {
            presenterScope.launch {
                _screenViewState.emit(SessionViewState.Error(DomainError.SESSION_NOT_FOUND.code))
            }
        } else {
            logger.d("SessionPresenter", "pause::stopping stepTimer")
            stepTimerJob?.cancel()
            val currentStep = immutableSession.steps[currentSessionStepIndex]
            if (currentStep is SessionStep.WorkStep) {
                currentSessionStepIndex -= 1 // safe as the first step will always be a REST
            }
            presenterScope.launch {
                _dialogViewState.emit(SessionDialog.Pause)
            }
        }
    }

    fun resume() {
        val immutableSession = session
        if (immutableSession == null) {
            presenterScope.launch {
                _screenViewState.emit(SessionViewState.Error(DomainError.SESSION_NOT_FOUND.code))
            }
            return
        }
        val stepToStart = immutableSession.steps[currentSessionStepIndex]
        val remainingTotalTimeToLaunch =
            stepToStart.durationMs.plus(stepToStart.remainingSessionDurationMsAfterMe)
        stepTimerJob =
            presenterScope.launch {
                sessionInteractor.startStepTimer(totalMilliSeconds = remainingTotalTimeToLaunch)
            }
        presenterScope.launch {
            _dialogViewState.emit(SessionDialog.None)
        }
    }

    fun abortSession() {
        logger.d("SessionPresenter", "abortSession")
        stepTimerJob?.cancel()
        presenterScope.launch {
            emitSessionEndState()
            _dialogViewState.emit(SessionDialog.None)
        }
    }

    fun resetAndStart() {
        logger.d("SessionPresenter", "resetAndStart - cleaning up and starting fresh")
        resetInternalState()
        presenterScope.launch {
            _screenViewState.emit(SessionViewState.Loading)
            _dialogViewState.emit(SessionDialog.None)
        }
        initializeAndStartSession()
    }

    fun cleanup() {
        logger.d("SessionPresenter", "cleanup - cancelling all jobs and resetting state")
        resetInternalState()
        presenterScope.coroutineContext.cancelChildren()
    }

    /**
     * Resets internal state for a fresh session.
     * Used by both resetAndStart() and cleanup().
     */
    private fun resetInternalState() {
        stepTimerJob?.cancel()
        tickerCollectionJob?.cancel()
        session = null
        currentSessionStepIndex = 0
        sessionInteractor.resetTimerState()
    }
}
