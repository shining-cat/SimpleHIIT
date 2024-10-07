package fr.shiningcat.simplehiit.domain.session.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.TimeProvider
import fr.shiningcat.simplehiit.domain.common.di.TimerDispatcher
import fr.shiningcat.simplehiit.domain.common.models.StepTimerState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

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
        private var nextTickTimeStamp = 0L

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
                val expectedEndTimeMillis = startTimeStamp + totalMilliSeconds
                // emit starting state
                emit(
                    StepTimerState(
                        milliSecondsRemaining = totalMilliSeconds,
                        totalMilliSeconds = totalMilliSeconds,
                    ),
                )
                var totalReached = false
                var remainingMilliSeconds = totalMilliSeconds
                while (!totalReached) {
                    nextTickTimeStamp = timeProvider.getCurrentTimeMillis() + oneSecondAsMs
                    var secondComplete = false
                    while (!secondComplete) {
                        secondComplete = timeProvider.getCurrentTimeMillis() >= nextTickTimeStamp
                    }
                    remainingMilliSeconds -= oneSecondAsMs
                    // emit every second
                    emit(
                        StepTimerState(
                            milliSecondsRemaining = remainingMilliSeconds,
                            totalMilliSeconds = totalMilliSeconds,
                        ),
                    )
                    totalReached = timeProvider.getCurrentTimeMillis() >= expectedEndTimeMillis
                }
                // emit finish state
                emit(
                    StepTimerState(
                        milliSecondsRemaining = 0,
                        totalMilliSeconds = totalMilliSeconds,
                    ),
                )
            }
    }
