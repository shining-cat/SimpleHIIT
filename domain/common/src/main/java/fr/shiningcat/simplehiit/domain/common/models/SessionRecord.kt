package fr.shiningcat.simplehiit.domain.common.models

import fr.shiningcat.simplehiit.commonutils.annotations.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
data class SessionRecord(
    val id: Long = 0L,
    val timeStamp: Long,
    val durationMs: Long,
    val usersIds: List<Long>
)
