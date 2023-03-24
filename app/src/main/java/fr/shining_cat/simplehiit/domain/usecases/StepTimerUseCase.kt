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

    //TODO: the timer is drifting: every emission is 2 to 7ms late
    private var _timerStateFlow = MutableStateFlow(StepTimerState())
    val timerStateFlow: StateFlow<StepTimerState> = _timerStateFlow

    private var loggingTimestamp:Long = 0L
    private var stepStartTimeStamp = 0L

    suspend fun start(totalSeconds: Int) {
        stepStartTimeStamp = System.currentTimeMillis()
        hiitLogger.d("StepTimerUseCase", "-------------- START --------------")
        return withContext(timerDispatcher) {
            loggingTimestamp = System.currentTimeMillis()
            initTimer(totalSeconds)
                .onCompletion {
                    if(currentCoroutineContext().isActive) {
                        hiitLogger.d(
                            "StepTimerUseCase",
                            "onCompletion:: actual step length = ${System.currentTimeMillis() - stepStartTimeStamp} | expected was: ${totalSeconds * 1000}"
                        )
                    }
                }
                .collect {
                    hiitLogger.d("StepTimerUseCase","COLLECT: time since step start: ${System.currentTimeMillis() - stepStartTimeStamp}")//todo: here we are 1ms more late
                    _timerStateFlow.emit(it) //TODO: this emission is what will be examined and will trigger the next step launch, so any deviation at this stage will be cumulating over the whole session!
                }
        }
    }

    private val timerHeartBeatRatio: Int = 100
    private val oneSecondAsMs: Long = 1000L

    private fun initTimer(totalSeconds: Int): Flow<StepTimerState> =
        ((totalSeconds - 1).times(timerHeartBeatRatio) downTo 0).asFlow() // first emit total - 1 because the total is emitted by onStart
            .onEach { delay(oneSecondAsMs.div(timerHeartBeatRatio)) } // internal heartbeat of 1ms
            .onStart {
                emit(totalSeconds.times(timerHeartBeatRatio))
            } // Emit total seconds immediately, without waiting the specified delay in onEach
            .conflate() // In case the operation in onTransform takes some time, conflate keeps the time ticking separately
            .onEach { delay(oneSecondAsMs) }//only collect and reemit every second
            .transform { remainingHeartBeats: Int ->
                val remainingSeconds = remainingHeartBeats.div(timerHeartBeatRatio.toFloat()).toInt() //TODO: this rounding will hopefully correct for the drift?
                val now = System.currentTimeMillis() //todo: here we are already from 2ms to 5ms late
                hiitLogger.d("StepTimerUseCase","TICKING: remainingHeartBeats = $remainingHeartBeats | remainingSeconds = $remainingSeconds | tick length = ${now - loggingTimestamp} | time since step start: ${System.currentTimeMillis() - stepStartTimeStamp}")
                loggingTimestamp = now
                emit(StepTimerState(remainingSeconds, totalSeconds))
            }
            .flowOn(timerDispatcher)//actually ensure the operation will flow on the provided dispatcher. This mostly allows testing the usecase by manipulating it

}