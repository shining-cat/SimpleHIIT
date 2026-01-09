package fr.shiningcat.simplehiit.domain.session.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.TimeProvider
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
import kotlin.coroutines.coroutineContext
import kotlin.math.max

class StepTimerUseCase(
    private val timerDispatcher: CoroutineDispatcher,
    private val timeProvider: TimeProvider,
    private val logger: HiitLogger,
) {
    private val _timerStateFlow = MutableStateFlow(StepTimerState())
    val timerStateFlow: StateFlow<StepTimerState> = _timerStateFlow

    private var startTimeStamp = 0L

    suspend fun start(totalMilliSeconds: Long) {
        logger.d("StepTimerUseCase", "start::totalMilliSeconds = $totalMilliSeconds")
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
                logger.d("StepTimerUseCase", "initTimer::ticker loop::adjustedDelay = $adjustedDelay")
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

    /**
     * Resets the timer state to default values.
     * Should be called when cleaning up the session to prevent stale state.
     */
    fun reset() {
        logger.d("StepTimerUseCase", "reset - clearing timer state")
        _timerStateFlow.value = StepTimerState()
        startTimeStamp = 0L
    }
}
