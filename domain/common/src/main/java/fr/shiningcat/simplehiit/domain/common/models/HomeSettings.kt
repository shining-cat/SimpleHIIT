package fr.shiningcat.simplehiit.domain.common.models

data class HomeSettings(
    val numberCumulatedCycles: Int,
    val cycleLengthMs: Long,
    val users: List<User>,
    val warning: LaunchSessionWarning? = null,
)
