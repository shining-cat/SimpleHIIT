package fr.shining_cat.simplehiit.domain.common.models

import fr.shining_cat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
enum class ExerciseSide { NONE, LEFT, RIGHT }
@ExcludeFromJacocoGeneratedReport
enum class AsymmetricalExerciseSideOrder(val side: ExerciseSide) {
    FIRST(ExerciseSide.RIGHT),
    SECOND(ExerciseSide.LEFT)
}

@ExcludeFromJacocoGeneratedReport
enum class SessionStepType { WORK, REST, PREPARE }

@ExcludeFromJacocoGeneratedReport
sealed class SessionStep(
    open val durationMs: Long,
    open val remainingSessionDurationMsAfterMe: Long,
    open val countDownLengthMs: Long
) {

    @ExcludeFromJacocoGeneratedReport
    data class PrepareStep(
        override val durationMs: Long,
        override val remainingSessionDurationMsAfterMe: Long,
        override val countDownLengthMs: Long
    ) : SessionStep(
        durationMs,
        remainingSessionDurationMsAfterMe,
        countDownLengthMs
    )

    @ExcludeFromJacocoGeneratedReport
    data class WorkStep(
        val exercise: Exercise,
        val side: ExerciseSide,
        override val durationMs: Long,
        val durationFormatted: String, //TODO: this seems to not be used, if so remove
        override val remainingSessionDurationMsAfterMe: Long,
        override val countDownLengthMs: Long
    ) : SessionStep(
        durationMs,
        remainingSessionDurationMsAfterMe,
        countDownLengthMs
    )

    @ExcludeFromJacocoGeneratedReport
    data class RestStep(
        val exercise: Exercise,
        val side: ExerciseSide,
        override val durationMs: Long,
        val durationFormatted: String, //TODO: this seems to not be used, if so remove
        override val remainingSessionDurationMsAfterMe: Long,
        override val countDownLengthMs: Long
    ) : SessionStep(
        durationMs,
        remainingSessionDurationMsAfterMe,
        countDownLengthMs
    )
}

@ExcludeFromJacocoGeneratedReport
data class SessionStepDisplay(
    val exercise: Exercise,
    val side: ExerciseSide
)
