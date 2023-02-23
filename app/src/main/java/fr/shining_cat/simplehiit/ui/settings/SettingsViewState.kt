package fr.shining_cat.simplehiit.ui.settings

import fr.shining_cat.simplehiit.domain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.models.User

sealed class SettingsViewState{
    object SettingsLoading:SettingsViewState()
    data class SettingsNominal(
        val workPeriodLengthSeconds:Int,
        val restPeriodLengthSeconds:Int,
        val numberOfWorkPeriods:Int,
        val totalCycleLength:Int,
        val beepSoundCountDownActive:Boolean,
        val sessionStartCountDownLengthSeconds:Int,
        val periodsStartCountDownLengthSeconds:Int,
        val users:List<User>,
        val exerciseTypes: List<ExerciseTypeSelected>
    ):SettingsViewState()
    data class SettingsError(val errorCode: String):SettingsViewState()
}
