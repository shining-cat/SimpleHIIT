package fr.shiningcat.simplehiit.domain.session.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.TimeProvider
import fr.shiningcat.simplehiit.domain.common.di.TimerDispatcher
import fr.shiningcat.simplehiit.domain.common.models.StepTimerState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.math.max

class StepTimerUseCase
    @Inject
    constructor(
        @TimerDispatcher private val timerDispatcher: CoroutineDispatcher,
        private val timeProvider: TimeProvider,
        private val hiitLogger: HiitLogger,
    ) {
        private var _timerStateFlow = MutableStateFlow(StepTimerState())
        val timerStateFlow: StateFlow<StepTimerState> = _timerStateFlow

        private var startTimeStamp = 0L

        suspend fun start(totalMilliSeconds: Long) {
            hiitLogger.d("StepTimerUseCase", "start::totalMilliSeconds = $totalMilliSeconds")
            return withContext(timerDispatcher) {
                initTimer(totalMilliSeconds) // any work done in that Flow will be cancelled if the coroutine is cancelled
                    .flowOn(timerDispatcher)
                    .collect {
                        _timerStateFlow.emit(it)
                    }
            }
        }

        private val oneSecondAsMs = 1000L

        private fun initTimer(totalMilliSeconds: Long): Flow<StepTimerState> =
            flow {
                startTimeStamp = timeProvider.getCurrentTimeMillis()
                // emit starting state
                emit(
                    StepTimerState(
                        milliSecondsRemaining = totalMilliSeconds,
                        totalMilliSeconds = totalMilliSeconds,
                    ),
                )
                var remainingMs = totalMilliSeconds
                var elapsedTicks = 0
                while (remainingMs > 0L) {
                    // making the loop cancellation cooperative:
                    coroutineContext.ensureActive()
                    // adjusting each tick delay to prevent ticker drift:
                    elapsedTicks++
                    val nextTickWallClockTime = startTimeStamp + (elapsedTicks * oneSecondAsMs)
                    val currentTime = timeProvider.getCurrentTimeMillis()
                    val adjustedDelay = nextTickWallClockTime - currentTime
                    hiitLogger.d("StepTimerUseCase", "initTimer::ticker loop::adjustedDelay = $adjustedDelay")
                    if (adjustedDelay > 0) {
                        delay(adjustedDelay)
                    }
                    // If delayFor <= 0, we're already at or past the tick time, proceed to emit.

                    // Calculate the actual remaining time based on ticks passed, clamp to 0
                    val newRemainingMs = totalMilliSeconds - (elapsedTicks * oneSecondAsMs)
                    val emitMs = max(0L, newRemainingMs)

                    emit(
                        StepTimerState(
                            milliSecondsRemaining = emitMs,
                            totalMilliSeconds = totalMilliSeconds,
                        ),
                    )

                    remainingMs = emitMs // Update remainingMs for the loop
                }
            }
    }
