package fr.shiningcat.simplehiit.domain.common.models

data class User(
    val id: Long = 0L,
    val name: String,
    val selected: Boolean = true,
    val lastSessionTimestamp: Long? = null,
)
