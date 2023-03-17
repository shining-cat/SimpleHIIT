package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.models.StepTimerState
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class StepTimerUseCase @Inject constructor(private val hiitLogger: HiitLogger) {
    private var _timerStateFlow = MutableStateFlow(StepTimerState(-1))
    val timerStateFlow: StateFlow<StepTimerState> = _timerStateFlow

    suspend fun testStart(totalSeconds: Int) {
        return coroutineScope {
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
