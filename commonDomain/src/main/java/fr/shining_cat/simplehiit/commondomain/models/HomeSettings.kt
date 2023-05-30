package fr.shining_cat.simplehiit.commondomain.models

import fr.shining_cat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
data class HomeSettings(
    val numberCumulatedCycles: Int,
    val cycleLengthMs:Long,
    val users:List<User>
)
