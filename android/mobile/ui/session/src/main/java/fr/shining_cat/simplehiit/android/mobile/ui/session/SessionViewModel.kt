package fr.shining_cat.simplehiit.android.mobile.ui.session

import android.media.AudioAttributes
import android.media.SoundPool
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.commonutils.TimeProvider
import fr.shining_cat.simplehiit.commonutils.di.MainDispatcher
import fr.shining_cat.simplehiit.domain.common.Constants
import fr.shining_cat.simplehiit.domain.common.Output
import fr.shining_cat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shining_cat.simplehiit.domain.common.models.Session
import fr.shining_cat.simplehiit.domain.common.models.SessionRecord
import fr.shining_cat.simplehiit.domain.common.models.SessionStep
import fr.shining_cat.simplehiit.domain.common.models.SessionStepDisplay
import fr.shining_cat.simplehiit.domain.common.models.StepTimerState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionInteractor: SessionInteractor,
    private val mapper: SessionMapper,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val timeProvider: TimeProvider,
    private val hiitLogger: HiitLogger
) : ViewModel() {

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
    private var soundPool: SoundPool? = null
    private var beepSoundLoadedId: Int? = null
    var noSoundLoadingRequestedYet = true

    //
    fun init(durationStringFormatter: DurationStringFormatter) {
        if (!isInitialized) {
            hiitLogger.d("SessionViewModel", "initializing")
            this.durationStringFormatter = durationStringFormatter
            //
            setUpSoundPool()
            hiitLogger.d("SessionViewModel", "soundPool created, awaiting sound to be loaded to proceed")
            soundPool?.setOnLoadCompleteListener { _, _, _ ->
                hiitLogger.d("SessionViewModel", "sound loaded in soundPool, proceeding with SessionViewModel initialization...")
                setupTicker()
                //
                retrieveSettingsAndProceed()
            }
        }
        //
        isInitialized = true
    }

    private fun setUpSoundPool(){
        //This SoundPool is hosted in the ViewModel to shield it from recomposition events
        soundPool = SoundPool
            .Builder()
            .setMaxStreams(1) // we only ever need to play one sound at a time
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .build()
    }

    fun getSoundPool():SoundPool?{
        if(soundPool == null){
            hiitLogger.e("SessionViewModel", "getSoundPool::no SoundPool found!")
        }
        return soundPool
    }

    fun setLoadedSound(loadedSoundId: Int){
        // the sound is loaded from the SessionScreen as we need access to a Context and the raw resources,
        // but we will need its stream id to play it through the SoundPool
        hiitLogger.d("SessionViewModel", "setLoadedSound")
        this.beepSoundLoadedId = loadedSoundId
    }

    private fun setupTicker() {
        viewModelScope.launch(context = mainDispatcher) {
            sessionInteractor.getStepTimerState().collect { stepTimerState ->
                if (stepTimerState != StepTimerState()) { //excluding first emission with default value
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
                        session = sessionInteractor.buildSession(
                            sessionSettings = sessionSettingsResult,
                            durationStringFormatter = durationStringFormatter
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
            val wholeSessionDuration = immutableSession.durationMs
            stepTimerJob = viewModelScope.launch {
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
            if (sessionRemainingMs == 0L) {//whole session end
                //play last (when timer reaches 0) beep sound
                maybePlayBeepSound(forceBeep = immutableSession.beepSoundCountDownActive)
                hiitLogger.d("SessionViewModel","tick: Session finished")
                emitSessionEndState()
            } else {//build current running step state and emit
                val timeRemainingTriggerNextStep = currentStep.remainingSessionDurationMsAfterMe
                if (sessionRemainingMs <= timeRemainingTriggerNextStep) {
                    //play step's last beep sound as the increase of currentSessionStepIndex will prevent the state with a 0 countDown to be emitted
                    // we do not display the "0" remaining seconds, but we want the beep sound to be played if setting is on
                    maybePlayBeepSound(forceBeep = immutableSession.beepSoundCountDownActive)
                    currentSessionStepIndex += 1
                    hiitLogger.d(
                        "SessionViewModel",
                        "tick: step $currentStep has ended, incrementing currentSessionStepIndex to: $currentSessionStepIndex / ${immutableSession.steps.size - 1}"
                    )
                }
                viewModelScope.launch {
                    val currentState = mapper.buildStateWholeSession(
                        session = immutableSession,
                        currentSessionStepIndex = currentSessionStepIndex,
                        currentState = stepTimerState,
                        durationStringFormatter = durationStringFormatter
                    )
                    maybePlayBeepSound(currentState = currentState)
                    _screenViewState.emit(currentState)
                }
            }
        }
    }

    private fun maybePlayBeepSound(currentState: SessionViewState? = null, forceBeep: Boolean? = false){
        if(forceBeep == true) {
            playBeepSound()
            return
        }
        when(currentState){
            is SessionViewState.InitialCountDownSession -> if(currentState.countDown.playBeep) playBeepSound()
            is SessionViewState.RestNominal -> if(currentState.countDown?.playBeep == true) playBeepSound()
            is SessionViewState.WorkNominal -> if(currentState.countDown?.playBeep == true) playBeepSound()
            else -> {}// do nothing
        }
    }

    private fun playBeepSound(){
        val loadedSound = beepSoundLoadedId
        if(loadedSound == null){
            hiitLogger.e("SessionViewModel", "playBeepSound::no sound loaded in SoundPool! Has the sound been loaded through SessionViewModel.setLoadedSound?")
            return
        }
        soundPool?.play(loadedSound, 1f,1f,0,0,1f)
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
                    sessionInteractor.formatLongDurationMsAsSmallestHhMmSsString(
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
                    timeStamp = timeProvider.getCurrentTimeMillis(),
                    durationMs = actualSessionLength,
                    usersIds = session?.users?.map { it.id } ?: emptyList()
                )
                hiitLogger.d("SessionViewModel", "sessionRecord: $sessionRecord")
                sessionInteractor.insertSession(sessionRecord)
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
        val remainingTotalTimeToLaunch =
            stepToStart.durationMs.plus(stepToStart.remainingSessionDurationMsAfterMe)
        stepTimerJob = viewModelScope.launch(context = mainDispatcher) {
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
        isInitialized = false
        noSoundLoadingRequestedYet = true
        hiitLogger.d("SessionViewModel", "onCleared::cancelling stepTimerJob")
        stepTimerJob?.cancel()
        hiitLogger.d("SessionViewModel", "onCleared::releasing SoundPool")
        soundPool?.release()
    }

}