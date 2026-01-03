package fr.shiningcat.simplehiit.domain.common

data class DurationFormatPatterns(
    val hoursMinutesSeconds: String,
    val hoursMinutesNoSeconds: String,
    val hoursNoMinutesNoSeconds: String,
    val minutesSeconds: String,
    val minutesNoSeconds: String,
    val seconds: String,
)
