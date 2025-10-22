package fr.shiningcat.simplehiit.domain.common.models

data class SessionSettings(
    val numberCumulatedCycles: Int,
    val workPeriodLengthMs: Long,
    val restPeriodLengthMs: Long,
    val numberOfWorkPeriods: Int,
    val cycleLengthMs: Long,
    val beepSoundCountDownActive: Boolean,
    val sessionStartCountDownLengthMs: Long,
    val periodsStartCountDownLengthMs: Long,
    val users: List<User>,
    val exerciseTypes: List<ExerciseTypeSelected>,
)
