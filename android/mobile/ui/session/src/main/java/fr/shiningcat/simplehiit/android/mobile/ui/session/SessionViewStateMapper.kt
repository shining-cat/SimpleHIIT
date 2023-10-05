package fr.shiningcat.simplehiit.android.mobile.ui.session

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.di.DefaultDispatcher
import fr.shiningcat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shiningcat.simplehiit.domain.common.models.Session
import fr.shiningcat.simplehiit.domain.common.models.SessionStep
import fr.shiningcat.simplehiit.domain.common.models.StepTimerState
import fr.shiningcat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SessionViewStateMapper @Inject constructor(
    private val formatLongDurationMsAsSmallestHhMmSsStringUseCase: FormatLongDurationMsAsSmallestHhMmSsStringUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @Suppress("UNUSED_PARAMETER")
    private val hiitLogger: HiitLogger
) {

    suspend fun buildStateFromWholeSession(
        session: Session,
        currentSessionStepIndex: Int,
        currentStepTimerState: StepTimerState,
        durationStringFormatter: DurationStringFormatter
    ): SessionViewState {
        return withContext(defaultDispatcher) {
            val currentStep = session.steps[currentSessionStepIndex]
            val stepRemainingMilliSeconds =
                currentStepTimerState.milliSecondsRemaining.minus(currentStep.remainingSessionDurationMsAfterMe)
            val stepRemainingFormatted = formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                durationMs = stepRemainingMilliSeconds,
                durationStringFormatter = durationStringFormatter
            )
            val stepRemainingPercentage =
                stepRemainingMilliSeconds.div(currentStep.durationMs.toFloat())
            //
            val countdownMillis = currentStep.countDownLengthMs
            //we'll handle session start countdown separately below, it allows us to not handle this comparison as it's not relevant for that period
            val periodCountDown = if (stepRemainingMilliSeconds <= countdownMillis) {
                val countDownAsSecondsString =
                    stepRemainingMilliSeconds.div(1000L).toInt().toString()
                CountDown(
                    secondsDisplay = countDownAsSecondsString,
                    progress = stepRemainingMilliSeconds.div(countdownMillis.toFloat()),
                    playBeep = session.beepSoundCountDownActive
                )
            } else null
            //
            val sessionRemainingMilliSeconds = currentStepTimerState.milliSecondsRemaining
            val sessionRemainingFormatted =
                formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                    durationMs = sessionRemainingMilliSeconds,
                    durationStringFormatter = durationStringFormatter
                )
            //
            when (currentStep) {
                is SessionStep.PrepareStep -> {
                    val countDownAsSecondsString =
                        stepRemainingMilliSeconds.div(1000L).toInt().toString()
                    val sessionCountDown = CountDown(
                        secondsDisplay = countDownAsSecondsString,
                        progress = stepRemainingMilliSeconds.div(countdownMillis.toFloat()),
                        playBeep = session.beepSoundCountDownActive
                    )
                    SessionViewState.InitialCountDownSession(countDown = sessionCountDown)
                }

                is SessionStep.RestStep -> SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.REST,
                    displayedExercise = currentStep.exercise,
                    side = currentStep.side,
                    stepRemainingTime = stepRemainingFormatted,
                    stepRemainingPercentage = stepRemainingPercentage,
                    sessionRemainingTime = sessionRemainingFormatted,
                    sessionRemainingPercentage = currentStepTimerState.remainingPercentage,
                    countDown = periodCountDown,
                )

                is SessionStep.WorkStep -> SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.WORK,
                    displayedExercise = currentStep.exercise,
                    side = currentStep.side,
                    stepRemainingTime = stepRemainingFormatted,
                    stepRemainingPercentage = stepRemainingPercentage,
                    sessionRemainingTime = sessionRemainingFormatted,
                    sessionRemainingPercentage = currentStepTimerState.remainingPercentage,
                    countDown = periodCountDown,
                )
                /*
                                is SessionStep.RestStep -> SessionViewState.RestNominal(
                                    nextExercise = currentStep.exercise,
                                    side = currentStep.side,
                                    restRemainingTime = stepRemainingFormatted,
                                    restRemainingPercentage = stepRemainingPercentage,
                                    sessionRemainingTime = sessionRemainingFormatted,
                                    sessionRemainingPercentage = currentStepTimerState.remainingPercentage,
                                    countDown = periodCountDown,
                                )

                                is SessionStep.WorkStep -> SessionViewState.WorkNominal(
                                    currentExercise = currentStep.exercise,
                                    side = currentStep.side,
                                    exerciseRemainingTime = stepRemainingFormatted,
                                    exerciseRemainingPercentage = stepRemainingPercentage,
                                    sessionRemainingTime = sessionRemainingFormatted,
                                    sessionRemainingPercentage = currentStepTimerState.remainingPercentage,
                                    countDown = periodCountDown,
                                )
                */

            }
        }
    }
}