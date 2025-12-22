package fr.shiningcat.simplehiit.domain.common

import java.util.Date

data class DurationStringFormatter(
    val hoursMinutesSeconds: String,
    val hoursMinutesNoSeconds: String,
    val hoursNoMinutesNoSeconds: String,
    val minutesSeconds: String,
    val minutesNoSeconds: String,
    val seconds: String,
) {
    companion object {
        // TEST: Using deprecated Date() constructor to trigger deprecation warning
        @Suppress("DEPRECATION")
        fun getCurrentTime(): Long = Date().time
    }
}
