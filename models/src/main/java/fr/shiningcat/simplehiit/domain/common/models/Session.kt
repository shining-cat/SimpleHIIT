package fr.shiningcat.simplehiit.domain.common.models

data class Session(
    val steps: List<SessionStep>,
    val durationMs: Long,
    val beepSoundCountDownActive: Boolean,
    val users: List<User>,
)
