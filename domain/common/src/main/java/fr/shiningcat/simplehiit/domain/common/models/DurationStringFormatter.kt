package fr.shiningcat.simplehiit.domain.common.models

import fr.shiningcat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
data class DurationStringFormatter(
    val hoursMinutesSeconds: String = "%1\$02d:%2\$02d:%3\$02d",
    val hoursMinutesNoSeconds: String = hoursMinutesSeconds,
    val hoursNoMinutesNoSeconds: String = hoursMinutesSeconds,
    val minutesSeconds: String = "%1\$02d:%2\$02d",
    val minutesNoSeconds: String = minutesSeconds,
    val seconds: String = "%02d"
)
