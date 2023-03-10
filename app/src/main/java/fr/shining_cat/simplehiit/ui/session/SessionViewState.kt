package fr.shining_cat.simplehiit.ui.session

import fr.shining_cat.simplehiit.domain.models.Exercise

sealed class SessionViewState{
    object SessionLoading:SessionViewState()
    data class SessionWorkNominal(
        val currentExercise: Exercise,
        val exerciseRemainingTime:String,
        val exerciseProgress:Int,
        val sessionRemainingTime:String,
        val sessionProgress:Int
    ):SessionViewState()
    data class SessionRestNominal(
        val nextExercise: Exercise,
        val restRemainingTime:String,
        val restProgress:Int,
        val sessionRemainingTime:String,
        val sessionProgress:Int
    ):SessionViewState()
    data class SessionFinished(
        val sessionDurationFormatted: String,
        val exercisesDone: List<String>
    ):SessionViewState()
    data class SessionErrorState(val errorCode: String):SessionViewState()
}

sealed class SessionDialog() {
    object None : SessionDialog()
    object PauseDialog: SessionDialog()
}