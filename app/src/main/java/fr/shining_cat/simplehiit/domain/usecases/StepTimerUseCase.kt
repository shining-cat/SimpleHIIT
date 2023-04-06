package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.di.TimerDispatcher
import fr.shining_cat.simplehiit.domain.models.StepTimerState
import fr.shining_cat.simplehiit.utils.HiitLogger
import fr.shining_cat.simplehiit.utils.TimeProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class StepTimerUseCase @Inject constructor(
    @TimerDispatcher private val timerDispatcher: CoroutineDispatcher,
    private val timeProvider: TimeProvider,
    private val hiitLogger: HiitLogger
) {

    private var _timerStateFlow = MutableStateFlow(StepTimerState())
    val timerStateFlow: StateFlow<StepTimerState> = _timerStateFlow

    private var startTimeStamp = 0L
    private var nextTickTimeStamp = 0L

    suspend fun start(totalMilliSeconds: Long) {
        hiitLogger.d("StepTimerUseCase","start::totalMilliSeconds = $totalMilliSeconds")
        return withContext(timerDispatcher) {
            initTimer(totalMilliSeconds)//any work done in that Flow will be cancelled if the coroutine is cancelled
                .flowOn(timerDispatcher)
                .collect {
                    _timerStateFlow.emit(it)
                }
        }
    }

    private val oneSecondAsMs = 1000L

    private fun initTimer(totalMilliSeconds: Long): Flow<StepTimerState> = flow {
        startTimeStamp = timeProvider.getCurrentTimeMillis()
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
            nextTickTimeStamp = timeProvider.getCurrentTimeMillis() + oneSecondAsMs
            var secondComplete = false
            while (!secondComplete) {
                secondComplete = timeProvider.getCurrentTimeMillis() >= nextTickTimeStamp
            }
            remainingMilliSeconds -= oneSecondAsMs
            //emit every second
            emit(
                StepTimerState(
                    milliSecondsRemaining = remainingMilliSeconds,
                    totalMilliSeconds = totalMilliSeconds
                )
            )
            totalReached = timeProvider.getCurrentTimeMillis() >= expectedEndTimeMillis
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