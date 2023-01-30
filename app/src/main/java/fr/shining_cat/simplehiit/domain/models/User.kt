package fr.shining_cat.simplehiit.domain.models

import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
data class User(
    val id:Long = 0L,
    val name:String,
    val selected:Boolean
)