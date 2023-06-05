package fr.shining_cat.simplehiit.android.mobile.ui.session

import fr.shining_cat.simplehiit.commondomain.models.Exercise
import fr.shining_cat.simplehiit.commondomain.models.ExerciseSide
import fr.shining_cat.simplehiit.commondomain.models.SessionStepDisplay

sealed class SessionViewState {
    object Loading : SessionViewState()
    data class InitialCountDownSession(
        val countDown: CountDown
    ) : SessionViewState()

    data class WorkNominal(
        val currentExercise: Exercise,
        val side: ExerciseSide,
        val exerciseRemainingTime: String,
        val exerciseRemainingPercentage: Float,
        val sessionRemainingTime: String,
        val sessionRemainingPercentage: Float,
        val countDown: CountDown? = null
    ) : SessionViewState()

    data class RestNominal(
        val nextExercise: Exercise,
        val side: ExerciseSide,
        val restRemainingTime: String,
        val restRemainingPercentage: Float,
        val sessionRemainingTime: String,
        val sessionRemainingPercentage: Float,
        val countDown: CountDown? = null
    ) : SessionViewState()

    data class Finished(
        val sessionDurationFormatted: String,
        val workingStepsDone: List<SessionStepDisplay>
    ) : SessionViewState()

    data class Error(val errorCode: String) : SessionViewState()
}

data class CountDown(
    val secondsDisplay: String,
    val progress: Float,
    val playBeep: Boolean
)

sealed class SessionDialog() {
    object None : SessionDialog()
    object Pause : SessionDialog()
}