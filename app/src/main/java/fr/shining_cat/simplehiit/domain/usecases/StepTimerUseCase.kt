package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.di.TimerDispatcher
import fr.shining_cat.simplehiit.domain.models.StepTimerState
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class StepTimerUseCase @Inject constructor(
    @TimerDispatcher private val timerDispatcher: CoroutineDispatcher,
    private val hiitLogger: HiitLogger
) {

    private var _timerStateFlow = MutableStateFlow(StepTimerState())
    val timerStateFlow: StateFlow<StepTimerState> = _timerStateFlow

    private var startTimeStamp = 0L
    private var nextTickTimeStamp = 0L

    suspend fun start(totalMilliSeconds: Long) {
        startTimeStamp = System.currentTimeMillis()
        return withContext(timerDispatcher) {
            initTimer(totalMilliSeconds)//any work done in that Flow will be cancelled if the coroutine is cancelled
                .collect {
                    _timerStateFlow.emit(it)
                }
        }
    }

    private val oneSecondAsMs = 1000L

    private fun initTimer(totalMilliSeconds: Long): Flow<StepTimerState> = flow {
        startTimeStamp = System.currentTimeMillis()
        val expectedEndTimeMillis = startTimeStamp + totalMilliSeconds
        //emit starting state
        emit(
            StepTimerState(
                milliSecondsRemaining = totalMilliSeconds,
                totalMilliSeconds = totalMilliSeconds
            )
        )
        var totalReached = false
        var remainingMilliSeconds = totalMilliSeconds
        while (!totalReached) {
            nextTickTimeStamp = System.currentTimeMillis() + oneSecondAsMs
            var secondComplete = false
            while (!secondComplete) {
                secondComplete = System.currentTimeMillis() >= nextTickTimeStamp
            }
            remainingMilliSeconds -= oneSecondAsMs
            //emit every second
            emit(
                StepTimerState(
                    milliSecondsRemaining = remainingMilliSeconds,
                    totalMilliSeconds = totalMilliSeconds
                )
            )
            totalReached = System.currentTimeMillis() >= expectedEndTimeMillis
        }
        //emit finish state
        emit(
            StepTimerState(
                milliSecondsRemaining = 0,
                totalMilliSeconds = totalMilliSeconds
            )
        )
    }
}