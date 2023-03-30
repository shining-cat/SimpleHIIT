package fr.shining_cat.simplehiit.ui.session

import fr.shining_cat.simplehiit.domain.models.Exercise
import fr.shining_cat.simplehiit.domain.models.ExerciseSide
import fr.shining_cat.simplehiit.domain.models.SessionStepDisplay

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
        val restProgress: Float,
        val sessionRemainingTime: String,
        val sessionProgress: Float,
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