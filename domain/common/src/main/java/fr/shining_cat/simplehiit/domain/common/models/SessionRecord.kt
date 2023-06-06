package fr.shining_cat.simplehiit.domain.common.models

import fr.shining_cat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
data class SessionRecord(
    val id:Long = 0L,
    val timeStamp:Long,
    val durationMs:Long,
    val usersIds:List<Long>
)
