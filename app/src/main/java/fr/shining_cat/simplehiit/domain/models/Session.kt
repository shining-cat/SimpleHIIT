package fr.shining_cat.simplehiit.domain.models

import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
data class Session(
    val id:Long = 0L,
    val date:Long,
    val duration:Long,
    val usersIds:List<Long>
)
