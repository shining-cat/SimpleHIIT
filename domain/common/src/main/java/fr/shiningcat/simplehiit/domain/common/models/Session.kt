package fr.shiningcat.simplehiit.domain.common.models

import fr.shiningcat.simplehiit.commonutils.annotations.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
data class Session(
    val steps: List<SessionStep>,
    val durationMs: Long,
    val beepSoundCountDownActive: Boolean,
    val users: List<User>
)
