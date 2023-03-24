package fr.shining_cat.simplehiit.ui.session

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.di.DefaultDispatcher
import fr.shining_cat.simplehiit.di.IoDispatcher
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.*
import fr.shining_cat.simplehiit.domain.usecases.BuildSessionUseCase
import fr.shining_cat.simplehiit.domain.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shining_cat.simplehiit.domain.usecases.GetSessionSettingsUseCase
import fr.shining_cat.simplehiit.domain.usecases.StepTimerUseCase
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
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
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

    // drift analysis:
    private var sessionStartTimestamp = 0L
    private data class DriftAnalysis( val step: String, val elapsedTimeSincePrevious: Long, val elapsedTimeSinceStart: Long)
    private val analysis = mutableListOf<DriftAnalysis>()
    private fun logDriftAnalysis(step: String){
        val elapsedTotal = System.currentTimeMillis() - sessionStartTimestamp
        val elapsedSinceLast = elapsedTotal - (analysis.lastOrNull()?.elapsedTimeSinceStart ?: 0L)
        analysis.add(DriftAnalysis(
            step = step,
            elapsedTimeSincePrevious = elapsedSinceLast,
            elapsedTimeSinceStart = elapsedTotal
        ))
    }
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
        viewModelScope.launch {
            stepTimerUseCase.timerStateFlow.collect() { stepTimerState ->
                /*hiitLogger.d(
                    "SessionViewModel",
                    "setupStepTimer::timerStateFlow::stepTimerState::secondsRemaining = ${stepTimerState.secondsRemaining}"
                )*/
                //logDriftAnalysis("timerStateFlow.collect:secondsRemaining = ${stepTimerState.secondsRemaining}")
                if(stepTimerState != StepTimerState()) { //excluding first emission with default value
                    tick(stepTimerState)
                }
            }
        }
    }

    private fun retrieveSettingsAndProceed() {
        logDriftAnalysis("retrieveSettingsAndProceed START")
        viewModelScope.launch {
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
                        logDriftAnalysis("retrieveSettingsAndProceed settings retrieved")
                        val sessionSettingsResult = sessionSettingsOutput.result

                        logDriftAnalysis("retrieveSettingsAndProceed buildSessionUseCase.execute")
                        session = buildSessionUseCase.execute(
                            sessionSettings = sessionSettingsResult,
                            durationStringFormatter = durationStringFormatter
                        )
                        /*hiitLogger.d(
                            "SessionViewModel",
                            "init::session built! contains ${session?.steps?.size} steps"
                        )*/
                        sessionStartTimestamp = System.currentTimeMillis()
                        launchSessionStep()
                    }
                }
            }
        }
    }

    private fun tick(stepTimerState: StepTimerState) {
        val immutableSession = session
        if (immutableSession == null) {
            hiitLogger.e("SessionViewModel", "tick::session is NULL!")
            viewModelScope.launch {
                _screenViewState.emit(SessionViewState.Error(Constants.Errors.SESSION_NOT_FOUND.code))
            }
        } else {
            val currentStep = immutableSession.steps[currentSessionStepIndex]
            logDriftAnalysis("tick currentStep: ${currentStep::class.java.simpleName}")
            val remainingSeconds = stepTimerState.secondsRemaining
            if (remainingSeconds == 0) {//step end
                stepTimerJob?.cancel()
                if (immutableSession.steps.lastOrNull() == currentStep) {
                    emitSessionEndState()
                } else {
                    //session is not finished, increment step index and continue
                    //we don't emit any state here as we expect the next step's first state to be emitted immediately
                    //logDriftAnalysis("tick::STEP ended")
                    currentSessionStepIndex += 1
                    launchSessionStep()
                }
            } else {//build running step state and emit
                viewModelScope.launch {
                    //logDriftAnalysis("tick::normal ticking - mapper.buildState START")
                    val currentState = mapper.buildState(
                        session = immutableSession,
                        currentSessionStepIndex = currentSessionStepIndex,
                        currentState = stepTimerState,
                        durationStringFormatter = durationStringFormatter
                    )
                    //logDriftAnalysis("tick::normal ticking - mapper.buildState END -> viewstate emission")
                    _screenViewState.emit(currentState)
                }
            }
        }
    }

    private fun emitSessionEndState() {
        viewModelScope.launch {
            //logDriftAnalysis("emitSessionEndState START")
            val immutableSession = session
            if (immutableSession == null) {
                hiitLogger.e("SessionViewModel", "emitSessionEndState::session is NULL!")
                _screenViewState.emit(SessionViewState.Error(Constants.Errors.SESSION_NOT_FOUND.code))
            } else {
                val totalElapsedTime = (System.currentTimeMillis() - sessionStartTimestamp)
                /*hiitLogger.d(
                    "SessionViewModel",
                    "emitSessionEndState::totalElapsedTime Ms = $totalElapsedTime"
                )
                hiitLogger.d(
                    "SessionViewModel",
                    "emitSessionEndState::expected duration Ms = ${immutableSession.durationMs}"
                )*/
                hiitLogger.d(
                    "SessionViewModel",
                    "emitSessionEndState::drift = ${totalElapsedTime - immutableSession.durationMs}"
                )
                if(immutableSession.steps.last() is SessionStep.RestStep){
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
                //logDriftAnalysis("emitSessionEndState END -> viewstate emission")
                _screenViewState.emit(
                    SessionViewState.Finished(
                        sessionDurationFormatted = actualSessionLengthFormatted,
                        workingStepsDone = workingStepsDoneDisplay
                    )
                )
                for(drift in analysis){
                    hiitLogger.d(
                        "SessionViewModel",
                        "time since previous log: ${drift.elapsedTimeSincePrevious} - time since start : ${drift.elapsedTimeSinceStart}  -  ${drift.step}"
                    )
                }
            }
        }
    }

    private fun launchSessionStep() {
        /*hiitLogger.d(
            "SessionViewModel",
            "launchSessionStep::stepIndex = $currentSessionStepIndex"
        )*/
        val immutableSession = session
        if (immutableSession == null) {
            viewModelScope.launch {
                _screenViewState.emit(SessionViewState.Error(Constants.Errors.SESSION_NOT_FOUND.code))
            }
            return
        }
        val stepToStart = immutableSession.steps[currentSessionStepIndex]
        logDriftAnalysis("launchSessionStep START ${stepToStart::class.java.simpleName}")
        val stepDurationS = stepToStart.durationMs.div(1000L).toInt()
        stepTimerJob = viewModelScope.launch {
            //logDriftAnalysis("launchSessionStep::stepTimerUseCase.start")
            stepTimerUseCase.start(stepDurationS)
        }
    }

    fun pause() {
        hiitLogger.d("SessionViewModel", "pause")
        viewModelScope.launch {
            val immutableSession = session
            if (immutableSession == null) {
                _screenViewState.emit(SessionViewState.Error(Constants.Errors.SESSION_NOT_FOUND.code))
            } else {
                hiitLogger.d("SessionViewModel", "pause::stopping stepTimer")
                stepTimerJob?.cancel()
                val currentStep = immutableSession.steps[currentSessionStepIndex]
                if (currentStep is SessionStep.WorkStep) {
                    /*hiitLogger.d(
                        "SessionViewModel",
                        "pause::reset current WORK step to last REST step"
                    )*/
                    currentSessionStepIndex -= 1 //safe as the first step will always be a REST
                }
                _dialogViewState.emit(SessionDialog.Pause)
            }
        }
    }

    fun resume() {
//        hiitLogger.d("SessionViewModel", "resume")
        viewModelScope.launch {
            launchSessionStep()
            _dialogViewState.emit(SessionDialog.None)
        }
    }

    fun abortSession() {
//        hiitLogger.d("SessionViewModel", "abortSession")
        viewModelScope.launch {
            emitSessionEndState()
            _dialogViewState.emit(SessionDialog.None)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stepTimerJob?.cancel()
    }

}