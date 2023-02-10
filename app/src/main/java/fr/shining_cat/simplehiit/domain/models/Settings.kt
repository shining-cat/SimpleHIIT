package fr.shining_cat.simplehiit.domain.models

data class Settings(
    val workPeriodLengthSeconds:Int,
    val restPeriodLengthSeconds:Int,
    val numberOfWorkPeriods:Int,
    val beepSoundCountDownActive:Boolean,
    val sessionStartCountDownLengthSeconds:Int,
    val periodsStartCountDownLengthSeconds:Int,
    val users:List<User>,
    val exerciseTypes: List<ExerciseTypeSelected>
)
