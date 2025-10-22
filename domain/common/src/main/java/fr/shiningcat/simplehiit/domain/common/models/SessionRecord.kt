package fr.shiningcat.simplehiit.domain.common.models

data class SessionRecord(
    val id: Long = 0L,
    val timeStamp: Long,
    val durationMs: Long,
    val usersIds: List<Long>,
)
