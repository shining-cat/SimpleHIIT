package fr.shining_cat.simplehiit.domain.common.usecases

import fr.shining_cat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FormatLongDurationMsAsSmallestHhMmSsStringUseCase @Inject constructor(
    private val hiitLogger: HiitLogger
) {

    /**
     * format a Long duration in Milliseconds to a HMS String
     * no leading zero, zero smallest units are removed if corresponding string formats are provided
     * examples of formatting:
     * value in MS -> return with provided string formats / return with default values
     * arguments all have default values leading to the basic Hh:Mm:Ss formatting
     * using the default formatting will not remove leading 0 and the zero smallest units
     * See unit tests for examples of formatting values and the expected result
     **/
    fun execute(
        durationMs: Long,
        durationStringFormatter: DurationStringFormatter
    ): String {

        val hours = TimeUnit.MILLISECONDS.toHours(durationMs)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60

        return when {
            // ex: 5s / 32s
            hours == 0L && minutes == 0L -> durationStringFormatter.seconds.format(seconds)
            //
            hours == 0L && minutes > 0L -> when (seconds) {
                // ex: 3mn / 13mn
                0L -> try {
                    durationStringFormatter.minutesNoSeconds.format(
                        minutes
                    )
                }
                // the default String needs the 0 seconds argument to be formatted
                catch (mae: MissingFormatArgumentException) {
                    durationStringFormatter.minutesNoSeconds.format(
                        minutes,
                        seconds
                    )
                }
                // ex: 2mn04s / 1mn43s / 23mn52s
                else -> durationStringFormatter.minutesSeconds.format(
                    minutes,
                    seconds
                )
            }
            else -> when (seconds) {
                0L -> try {
                    when (minutes) {
                        // ex: 1h / 23h
                        0L -> durationStringFormatter.hoursNoMinutesNoSeconds.format(hours)
                        // ex: 2h03mn / 3h45mn
                        else -> durationStringFormatter.hoursMinutesNoSeconds.format(
                            hours,
                            minutes
                        )
                    }
                }
                // the default String needs the 0 minutes & 0 seconds arguments to be formatted
                catch (mae: MissingFormatArgumentException) {
                    durationStringFormatter.hoursMinutesSeconds.format(
                        hours,
                        minutes,
                        seconds
                    )
                }
                // ex: 3h04mn05s / 4h12mn34s / 12h34mn56s
                else -> durationStringFormatter.hoursMinutesSeconds.format(
                    hours,
                    minutes,
                    seconds
                )
            }
        }

    }
}