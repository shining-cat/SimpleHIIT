package fr.shining_cat.simplehiit.domain.models

data class HomeSettings(
    val numberCumulatedCycles: Int,
    val cycleLengthMs:Long,
    val users:List<User>
)
