package fr.shining_cat.simplehiit.domain.models

data class StepTimerState(
    val secondsRemaining: Int,
    val totalSeconds: Int = 60,
) {

    val remainingPercentage: Float = secondsRemaining / totalSeconds.toFloat()

}