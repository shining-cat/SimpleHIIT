package fr.shiningcat.simplehiit.domain.common

import fr.shiningcat.simplehiit.commonutils.annotations.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
data class DurationStringFormatter(
    val hoursMinutesSeconds: String,
    val hoursMinutesNoSeconds: String,
    val hoursNoMinutesNoSeconds: String,
    val minutesSeconds: String,
    val minutesNoSeconds: String,
    val seconds: String,
)
