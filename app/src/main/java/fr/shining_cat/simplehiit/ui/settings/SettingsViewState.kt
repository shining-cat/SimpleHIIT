package fr.shining_cat.simplehiit.ui.settings

import fr.shining_cat.simplehiit.domain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.models.User

sealed class SettingsViewState{
    object SettingsLoading:SettingsViewState()
    data class SettingsNominal(
        val workPeriodLengthMs:Long,
        val restPeriodLengthMs:Long,
        val numberOfWorkPeriods:Int,
        val totalCycleLengthMs:Long,
        val beepSoundCountDownActive:Boolean,
        val sessionStartCountDownLengthMs:Long,
        val periodsStartCountDownLengthMs:Long,
        val users:List<User>,
        val exerciseTypes: List<ExerciseTypeSelected>
    ):SettingsViewState()
    data class SettingsError(val errorCode: String):SettingsViewState()
}
