package fr.shining_cat.simplehiit.domain.models

data class StepTimerState(
    val milliSecondsRemaining: Long = -1,
    val totalMilliSeconds: Long = -1,
) {

    val remainingPercentage: Float = milliSecondsRemaining / totalMilliSeconds.toFloat()

}