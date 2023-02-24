package fr.shining_cat.simplehiit.utils

import java.util.MissingFormatArgumentException
import java.util.concurrent.TimeUnit

/**
 * format a Long duration in Milliseconds to a HMS String
 * no leading zero, zero smallest units are removed if corresponding string formats are provided
 * examples of formatting:
 * value in MS -> return with provided string formats / return with default values
 * 5000L -> 5s / 05
 * 32000L -> 32s / 32
 * 180000L -> 3mn / 03:00
 * 780000L -> 13mn / 13:00
 * 124000L -> 2mn04s / 02:04
 * 103000L -> 1mn43s / 01:43
 * 3600000L -> 1h / 01:00:00
 * 57600000L -> 16h / 16:00:00
 * 7380000L -> 2h03mn / 02:03:00
 * 13500000L-> 3h45mn / 03:45:00
 * 11045000L -> 3h04mn05s / 03:04:05
 * 443096000L -> 123h 04mn 56s / 123:04:56
 * arguments all have default values leading to the basic Hh:Mm:Ss formatting
 * using the default formatting will not remove leading 0 and the zero smallest units
 * @param formatStringHoursMinutesSeconds: String
 * @param formatStringHoursMinutesNoSeconds: String
 * @param formatStringHoursNoMinutesNoSeconds: String
 * @param formatStringMinutesSeconds: String
 * @param formatStringMinutesNoSeconds: String
 * @param formatStringSeconds: String
 */
fun Long.autoFormatDurationMsAsSmallestHhMmSsString(
    formatStringHoursMinutesSeconds: String = "%1\$02d:%2\$02d:%3\$02d",
    formatStringHoursMinutesNoSeconds: String = formatStringHoursMinutesSeconds,
    formatStringHoursNoMinutesNoSeconds: String = formatStringHoursMinutesSeconds,
    formatStringMinutesSeconds: String = "%1\$02d:%2\$02d",
    formatStringMinutesNoSeconds: String = formatStringMinutesSeconds,
    formatStringSeconds: String = "%02d"
): String {
    val hours = TimeUnit.MILLISECONDS.toHours(this)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) % 60

    return when {
        // ex: 5s / 32s
        hours == 0L && minutes == 0L -> formatStringSeconds.format(seconds)
        //
        hours == 0L && minutes > 0L -> when (seconds) {
            // ex: 3mn / 13mn
            0L -> try {
                formatStringMinutesNoSeconds.format(
                    minutes
                )
            }
            // the default String needs the 0 seconds argument to be formatted
            catch (mae: MissingFormatArgumentException) {
                formatStringMinutesNoSeconds.format(
                    minutes,
                    seconds
                )
            }
            // ex: 2mn04s / 1mn43s / 23mn52s
            else -> formatStringMinutesSeconds.format(
                minutes,
                seconds
            )
        }
        else -> when (seconds) {
            0L -> try {
                when (minutes) {
                    // ex: 1h / 23h
                    0L -> formatStringHoursNoMinutesNoSeconds.format(hours)
                    // ex: 2h03mn / 3h45mn
                    else -> formatStringHoursMinutesNoSeconds.format(
                        hours,
                        minutes
                    )
                }
            }
            // the default String needs the 0 minutes & 0 seconds arguments to be formatted
            catch (mae: MissingFormatArgumentException) {
                formatStringHoursMinutesSeconds.format(
                    hours,
                    minutes,
                    seconds
                )
            }
            // ex: 3h04mn05s / 4h12mn34s / 12h34mn56s
            else -> formatStringHoursMinutesSeconds.format(
                hours,
                minutes,
                seconds
            )
        }
    }
}