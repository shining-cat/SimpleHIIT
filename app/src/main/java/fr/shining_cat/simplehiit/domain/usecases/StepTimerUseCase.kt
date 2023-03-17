package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.models.StepTimerState
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class StepTimerUseCase @Inject constructor(
    private val timerScope: CoroutineScope,
    private val hiitLogger: HiitLogger
) {
    private var _timerStateFlow = MutableStateFlow(StepTimerState(-1))
    val timerStateFlow: StateFlow<StepTimerState> = _timerStateFlow
    var isActive: Boolean = false

    private var job: Job? = null

    fun start(totalSeconds: Int) {
        hiitLogger.d("StepTimerUseCase","start::totalSeconds = $totalSeconds")
        if (job == null) {
            isActive = true
            job = timerScope.launch {
                initTimer(totalSeconds)
                    .onCompletion {
                        hiitLogger.d("StepTimerUseCase","onCompletion")
                        //_timerStateFlow.emit(StepTimerState(0)) //emitting 0 here causes erratic behaviour
                    }
                    .collect { _timerStateFlow.emit(it) }
            }
        } else {
            hiitLogger.e("StepTimerUseCase","start::improper start attempt: timer has not been stopped and might even still be running")
        }
    }

    fun stop(){
        hiitLogger.d("StepTimerUseCase","stop")
        if(job!=null){
            isActive = false
            job?.cancel()
            job = null
        } else {
            hiitLogger.e("StepTimerUseCase","stop::improper stop attempt: timer has not been started")
        }
    }

    /**
     * The timer emits the total seconds immediately.
     * Each second after that, it will emit the next value.
     */
    private fun initTimer(totalSeconds: Int): Flow<StepTimerState> =
        (totalSeconds - 1 downTo 0).asFlow() // Emit total - 1 because the first will be emitted onStart
            .onEach { delay(1000L) } // Each second later emit a number
            .onStart {
                hiitLogger.d("StepTimerUseCase","transform:totalSeconds = $totalSeconds")
                emit(totalSeconds) } // Emit total seconds immediately
            .conflate() // In case the operation onTick takes some time, conflate keeps the time ticking separately
            .transform { remainingSeconds: Int ->
                hiitLogger.d("StepTimerUseCase","transform:remainingSeconds = $remainingSeconds")
                emit(StepTimerState(remainingSeconds, totalSeconds))
            }
}
