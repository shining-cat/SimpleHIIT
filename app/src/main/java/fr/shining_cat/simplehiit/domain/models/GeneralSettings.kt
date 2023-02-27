package fr.shining_cat.simplehiit.domain.models

data class GeneralSettings(
    val workPeriodLengthMs:Long,
    val restPeriodLengthMs:Long,
    val numberOfWorkPeriods:Int,
    val beepSoundCountDownActive:Boolean,
    val sessionStartCountDownLengthMs:Long,
    val periodsStartCountDownLengthMs:Long,
    val users:List<User>,
    val exerciseTypes: List<ExerciseTypeSelected>
)