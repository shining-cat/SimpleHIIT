package fr.shining_cat.simplehiit.android.mobile.ui.session

import fr.shining_cat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.domain.common.models.Exercise
import fr.shining_cat.simplehiit.domain.common.models.ExerciseSide
import fr.shining_cat.simplehiit.domain.common.models.SessionStepDisplay

@ExcludeFromJacocoGeneratedReport
sealed interface SessionViewState {
    object Loading : SessionViewState
    data class InitialCountDownSession(
        val countDown: CountDown
    ) : SessionViewState

    data class RunningNominal(
        val periodType:RunningSessionStepType,
        val displayedExercise: Exercise,
        val side: ExerciseSide,
        val stepRemainingTime: String,
        val stepRemainingPercentage: Float,
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

enum class RunningSessionStepType{REST, WORK}

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