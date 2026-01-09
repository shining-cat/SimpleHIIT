package fr.shiningcat.simplehiit.sharedui.session

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.Session
import fr.shiningcat.simplehiit.domain.common.models.SessionStep
import fr.shiningcat.simplehiit.domain.common.models.StepTimerState
import fr.shiningcat.simplehiit.domain.common.usecases.DurationFormatStyle
import fr.shiningcat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SessionViewStateMapper(
    private val formatLongDurationMsAsSmallestHhMmSsStringUseCase: FormatLongDurationMsAsSmallestHhMmSsStringUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    @Suppress("UNUSED_PARAMETER")
    private val logger: HiitLogger,
) {
    suspend fun buildStateFromWholeSession(
        session: Session,
        currentSessionStepIndex: Int,
        currentStepTimerState: StepTimerState,
    ): SessionViewState =
        withContext(defaultDispatcher) {
            val currentStep = session.steps[currentSessionStepIndex]
            val stepRemainingMilliSeconds =
                currentStepTimerState.milliSecondsRemaining.minus(currentStep.remainingSessionDurationMsAfterMe)
            val stepRemainingFormatted =
                formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                    durationMs = stepRemainingMilliSeconds.coerceAtLeast(0L),
                    formatStyle = DurationFormatStyle.DIGITS_ONLY,
                )
            val stepRemainingPercentage =
                if (currentStep.durationMs > 0 && stepRemainingMilliSeconds >= 0) {
                    (stepRemainingMilliSeconds.div(currentStep.durationMs.toFloat())).coerceIn(0f, 1f)
                } else {
                    0f
                }
            //
            val countdownMillis = currentStep.countDownLengthMs
            // we'll handle session start countdown separately below, it allows us to not handle this comparison as it's not relevant for that period
            val periodCountDown =
                if (stepRemainingMilliSeconds <= countdownMillis) {
                    val countDownAsSecondsString =
                        stepRemainingMilliSeconds.div(1000L).toInt().toString()
                    CountDown(
                        secondsDisplay = countDownAsSecondsString,
                        progress = stepRemainingMilliSeconds.div(countdownMillis.toFloat()),
                        playBeep = session.beepSoundCountDownActive,
                    )
                } else {
                    null
                }
            //
            val sessionRemainingMilliSeconds = currentStepTimerState.milliSecondsRemaining
            val sessionRemainingFormatted =
                formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                    durationMs = sessionRemainingMilliSeconds,
                    formatStyle = DurationFormatStyle.DIGITS_ONLY,
                )
            //
            when (currentStep) {
                is SessionStep.PrepareStep -> {
                    val countDownAsSecondsString =
                        stepRemainingMilliSeconds.div(1000L).toInt().toString()
                    val sessionCountDown =
                        CountDown(
                            secondsDisplay = countDownAsSecondsString,
                            progress = stepRemainingMilliSeconds.div(countdownMillis.toFloat()),
                            playBeep = session.beepSoundCountDownActive,
                        )
                    SessionViewState.InitialCountDownSession(countDown = sessionCountDown)
                }
                is SessionStep.RestStep -> {
                    SessionViewState.RunningNominal(
                        periodType = RunningSessionStepType.REST,
                        displayedExercise = currentStep.exercise,
                        side = currentStep.side,
                        stepRemainingTime = stepRemainingFormatted,
                        stepRemainingPercentage = stepRemainingPercentage,
                        sessionRemainingTime = sessionRemainingFormatted,
                        sessionRemainingPercentage = currentStepTimerState.remainingPercentage,
                        countDown = periodCountDown,
                    )
                }
                is SessionStep.WorkStep -> {
                    SessionViewState.RunningNominal(
                        periodType = RunningSessionStepType.WORK,
                        displayedExercise = currentStep.exercise,
                        side = currentStep.side,
                        stepRemainingTime = stepRemainingFormatted,
                        stepRemainingPercentage = stepRemainingPercentage,
                        sessionRemainingTime = sessionRemainingFormatted,
                        sessionRemainingPercentage = currentStepTimerState.remainingPercentage,
                        countDown = periodCountDown,
                    )
                }
            }
        }
}
