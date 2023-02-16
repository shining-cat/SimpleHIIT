package fr.shining_cat.simplehiit.domain.models

import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.NUMBER_CUMULATED_CYCLES_DEFAULT

data class TotalRepetitionsSetting(
    val numberCumulatedCycles: Int = NUMBER_CUMULATED_CYCLES_DEFAULT
)