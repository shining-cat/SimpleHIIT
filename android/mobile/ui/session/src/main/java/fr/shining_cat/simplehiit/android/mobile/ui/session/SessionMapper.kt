package fr.shining_cat.simplehiit.android.mobile.ui.session

import fr.shining_cat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shining_cat.simplehiit.domain.common.models.Session
import fr.shining_cat.simplehiit.domain.common.models.SessionStep
import fr.shining_cat.simplehiit.domain.common.models.StepTimerState
import fr.shining_cat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.commonutils.di.DefaultDispatcher
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
            val remainingSeconds = currentStepTimerState.milliSecondsRemaining
            val countdownS = currentStep.countDownLengthMs.div(1000).toInt()
            val periodCountDown = if (remainingSeconds <= countdownS) {
                CountDown(
                    secondsDisplay = currentStepTimerState.milliSecondsRemaining.toString(),//not formatting the countdown value
                    progress = remainingSeconds.div(countdownS.toFloat()),
                    playBeep = session.beepSoundCountDownActive
                )
            } else null
            val stepRemainingAsMs = currentStepTimerState.milliSecondsRemaining.times(1000L)
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
                    countDown = periodCountDown,
                )

                is SessionStep.RestStep -> SessionViewState.RestNominal(
                    nextExercise = currentStep.exercise,
                    side = currentStep.side,
                    restRemainingTime = stepRemainingFormatted,
                    restRemainingPercentage = currentStepTimerState.remainingPercentage,
                    sessionRemainingTime = sessionRemainingFormatted,
                    sessionRemainingPercentage = sessionRemainingPercentage,
                    countDown = periodCountDown,
                )

                is SessionStep.PrepareStep -> {
                    val sessionCountDown = CountDown(
                        secondsDisplay = currentStepTimerState.milliSecondsRemaining.toString(),//not formatting the countdown value
                        progress = remainingSeconds.div(countdownS.toFloat()),
                        playBeep = session.beepSoundCountDownActive
                    )
                    SessionViewState.InitialCountDownSession(countDown = sessionCountDown)
                }
            }
        }
    }

    suspend fun buildStateWholeSession(
        session: Session,
        currentSessionStepIndex: Int,
        currentState: StepTimerState,
        durationStringFormatter: DurationStringFormatter
    ): SessionViewState {
        return withContext(defaultDispatcher) {
            val currentStep = session.steps[currentSessionStepIndex]
            val stepRemainingMilliSeconds =
                currentState.milliSecondsRemaining.minus(currentStep.remainingSessionDurationMsAfterMe)
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
            val sessionRemainingMilliSeconds = currentState.milliSecondsRemaining
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

                is SessionStep.RestStep -> SessionViewState.RestNominal(
                    nextExercise = currentStep.exercise,
                    side = currentStep.side,
                    restRemainingTime = stepRemainingFormatted,
                    restRemainingPercentage = stepRemainingPercentage,
                    sessionRemainingTime = sessionRemainingFormatted,
                    sessionRemainingPercentage = currentState.remainingPercentage,
                    countDown = periodCountDown,
                )

                is SessionStep.WorkStep -> SessionViewState.WorkNominal(
                    currentExercise = currentStep.exercise,
                    side = currentStep.side,
                    exerciseRemainingTime = stepRemainingFormatted,
                    exerciseRemainingPercentage = stepRemainingPercentage,
                    sessionRemainingTime = sessionRemainingFormatted,
                    sessionRemainingPercentage = currentState.remainingPercentage,
                    countDown = periodCountDown,
                )

            }
        }
    }
}