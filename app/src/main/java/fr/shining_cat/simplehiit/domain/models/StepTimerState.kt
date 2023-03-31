package fr.shining_cat.simplehiit.domain.models

data class StepTimerState(
    val secondsRemaining: Int = -1,
    val totalSeconds: Int = -1,
) {

    val remainingPercentage: Float = secondsRemaining / totalSeconds.toFloat()

}