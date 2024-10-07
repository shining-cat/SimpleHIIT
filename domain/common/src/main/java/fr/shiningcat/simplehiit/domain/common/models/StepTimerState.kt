package fr.shiningcat.simplehiit.domain.common.models

data class StepTimerState(
    val milliSecondsRemaining: Long = -1,
    val totalMilliSeconds: Long = -1,
) {
    // todo: add test
    val remainingPercentage: Float = milliSecondsRemaining / totalMilliSeconds.toFloat()
}
