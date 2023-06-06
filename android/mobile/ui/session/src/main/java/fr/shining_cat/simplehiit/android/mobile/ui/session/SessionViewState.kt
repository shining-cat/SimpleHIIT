package fr.shining_cat.simplehiit.android.mobile.ui.session

import fr.shining_cat.simplehiit.domain.common.models.Exercise
import fr.shining_cat.simplehiit.domain.common.models.ExerciseSide
import fr.shining_cat.simplehiit.domain.common.models.SessionStepDisplay
import fr.shining_cat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
sealed interface SessionViewState {
    object Loading : SessionViewState
    data class InitialCountDownSession(
        val countDown: CountDown
    ) : SessionViewState

    data class WorkNominal(
        val currentExercise: Exercise,
        val side: ExerciseSide,
        val exerciseRemainingTime: String,
        val exerciseRemainingPercentage: Float,
        val sessionRemainingTime: String,
        val sessionRemainingPercentage: Float,
        val countDown: CountDown? = null
    ) : SessionViewState

    data class RestNominal(
        val nextExercise: Exercise,
        val side: ExerciseSide,
        val restRemainingTime: String,
        val restRemainingPercentage: Float,
        val sessionRemainingTime: String,
        val sessionRemainingPercentage: Float,
        val countDown: CountDown? = null
    ) : SessionViewState

    data class Finished(
        val sessionDurationFormatted: String,
        val workingStepsDone: List<SessionStepDisplay>
    ) : SessionViewState

    data class Error(val errorCode: String) : SessionViewState
}

@ExcludeFromJacocoGeneratedReport
data class CountDown(
    val secondsDisplay: String,
    val progress: Float,
    val playBeep: Boolean
)

@ExcludeFromJacocoGeneratedReport
sealed interface SessionDialog {
    object None : SessionDialog
    object Pause : SessionDialog
}