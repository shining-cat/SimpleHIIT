package fr.shiningcat.simplehiit.domain.common.models

import fr.shiningcat.simplehiit.commonutils.annotations.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
data class HomeSettings(
    val numberCumulatedCycles: Int,
    val cycleLengthMs: Long,
    val users: List<User>
)
