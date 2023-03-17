package fr.shining_cat.simplehiit.domain.models

import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
data class SessionRecord(
    val id:Long = 0L,
    val timeStamp:Long,
    val durationMs:Long,
    val usersIds:List<Long>
)
