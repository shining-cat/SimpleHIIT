package fr.shining_cat.simplehiit.domain.common.models

import fr.shining_cat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
data class GeneralSettings(
    val workPeriodLengthMs:Long,
    val restPeriodLengthMs:Long,
    val numberOfWorkPeriods:Int,
    val cycleLengthMs:Long,
    val beepSoundCountDownActive:Boolean,
    val sessionStartCountDownLengthMs:Long,
    val periodsStartCountDownLengthMs:Long,
    val users:List<User>,
    val exerciseTypes: List<ExerciseTypeSelected>
)
