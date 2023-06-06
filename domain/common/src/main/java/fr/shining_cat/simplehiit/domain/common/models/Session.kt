package fr.shining_cat.simplehiit.domain.common.models

import fr.shining_cat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
data class Session(
    val steps: List<SessionStep>,
    val durationMs: Long,
    val beepSoundCountDownActive:Boolean,
    val users: List<User>
)