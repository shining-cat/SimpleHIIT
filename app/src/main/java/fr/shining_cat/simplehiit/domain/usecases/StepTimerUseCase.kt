package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.di.DefaultDispatcher
import fr.shining_cat.simplehiit.domain.models.StepTimerState
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StepTimerUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val hiitLogger: HiitLogger
) {

    //TODO: the timer is drifting: I got a cumulated drift of 39785ms (40s) for a session of 720000ms (12mn)
    private var _timerStateFlow = MutableStateFlow(StepTimerState(-1))
    val timerStateFlow: StateFlow<StepTimerState> = _timerStateFlow

    suspend fun testStart(totalSeconds: Int) {
        return withContext(defaultDispatcher) {
            initTimer(totalSeconds)
                .onCompletion {
                    hiitLogger.d("StepTimerUseCase", "onCompletion")
                    //_timerStateFlow.emit(StepTimerState(0)) //emitting 0 here causes erratic behaviour
                }
                .collect { _timerStateFlow.emit(it) }
        }
    }

    private fun initTimer(totalSeconds: Int): Flow<StepTimerState> =
        (totalSeconds - 1 downTo 0).asFlow() // Emit total - 1 because the first will be emitted onStart
            .onEach { delay(1000L) } // Each second later emit a number
            .onStart {
                hiitLogger.d("StepTimerUseCase", "transform:totalSeconds = $totalSeconds")
                emit(totalSeconds)
            } // Emit total seconds immediately
            .conflate() // In case the operation onTick takes some time, conflate keeps the time ticking separately
            .transform { remainingSeconds: Int ->
                hiitLogger.d("StepTimerUseCase", "transform:remainingSeconds = $remainingSeconds")
                emit(StepTimerState(remainingSeconds, totalSeconds))
            }
}
