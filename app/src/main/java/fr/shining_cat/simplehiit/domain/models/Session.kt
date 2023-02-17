package fr.shining_cat.simplehiit.domain.models

import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
data class Session(
    val id:Long = 0L,
    val timeStamp:Long,
    val durationSeconds:Long,
    val usersIds:List<Long>
)
