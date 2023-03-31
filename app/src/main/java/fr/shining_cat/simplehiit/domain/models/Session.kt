package fr.shining_cat.simplehiit.domain.models

data class Session(
    val steps: List<SessionStep>,
    val durationMs: Long,
    val durationFormatted: String,
    val beepSoundCountDownActive:Boolean,
    val users: List<User>
)