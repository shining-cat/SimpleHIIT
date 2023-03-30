package fr.shining_cat.simplehiit.ui.session

import fr.shining_cat.simplehiit.di.DefaultDispatcher
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.models.DurationStringFormatter
import fr.shining_cat.simplehiit.domain.models.Session
import fr.shining_cat.simplehiit.domain.models.SessionStep
import fr.shining_cat.simplehiit.domain.models.StepTimerState
import fr.shining_cat.simplehiit.domain.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SessionMapper @Inject constructor(
    private val formatLongDurationMsAsSmallestHhMmSsStringUseCase: FormatLongDurationMsAsSmallestHhMmSsStringUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val hiitLogger: HiitLogger
) {

    suspend fun buildState(
        session: Session,
        currentSessionStepIndex: Int,
        currentStepTimerState: StepTimerState,
        durationStringFormatter: DurationStringFormatter
    ): SessionViewState {
        return withContext(defaultDispatcher) {
            val currentStep = session.steps[currentSessionStepIndex]
            val remainingSeconds = currentStepTimerState.secondsRemaining
            val countdownS = currentStep.countDownLengthMs.div(1000).toInt()
            val countDown = if (remainingSeconds <= countdownS) {
                CountDown(
                    secondsDisplay = currentStepTimerState.secondsRemaining.toString(),//not formatting the countdown value
                    progress = remainingSeconds.div(countdownS.toFloat()),
                    playBeep = session.beepSoundCountDownActive
                )
            } else null
            val stepRemainingAsMs = currentStepTimerState.secondsRemaining.times(1000L)
            val stepRemainingFormatted =
                formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                    durationMs = stepRemainingAsMs,
                    durationStringFormatter = durationStringFormatter
                )
            val sessionRemaining =
                stepRemainingAsMs + currentStep.remainingSessionDurationMsAfterMe
            val sessionRemainingFormatted =
                formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                    durationMs = sessionRemaining,
                    durationStringFormatter = durationStringFormatter
                )
            val sessionRemainingPercentage =
                sessionRemaining.div(session.durationMs.toFloat())
            when (currentStep) {
                is SessionStep.WorkStep -> SessionViewState.WorkNominal(
                    currentExercise = currentStep.exercise,
                    side = currentStep.side,
                    exerciseRemainingTime = stepRemainingFormatted,
                    exerciseRemainingPercentage = currentStepTimerState.remainingPercentage,
                    sessionRemainingTime = sessionRemainingFormatted,
                    sessionRemainingPercentage = sessionRemainingPercentage,
                    countDown = countDown,
                )
                is SessionStep.RestStep -> SessionViewState.RestNominal(
                    nextExercise = currentStep.exercise,
                    side = currentStep.side,
                    restRemainingTime = stepRemainingFormatted,
                    restProgress = currentStepTimerState.remainingPercentage,
                    sessionRemainingTime = sessionRemainingFormatted,
                    sessionProgress = sessionRemainingPercentage,
                    countDown = countDown,
                )
                is SessionStep.PrepareStep -> {
                    if (countDown == null) {
                        //will never happen in real settings as we pick the same value for this
                        // step's length as the session countdown.
                        // Test cases though might encounter this error
                        SessionViewState.Error("${Constants.Errors.LAUNCH_SESSION.code} - The countdown length is shorter than the prepare step?")
                    } else {
                        SessionViewState.InitialCountDownSession(
                            countDown = countDown
                        )
                    }
                }
            }
        }
    }
}