package fr.shiningcat.simplehiit.domain.common.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.DurationFormatPatterns
import java.util.concurrent.TimeUnit

enum class DurationFormatStyle {
    DIGITS_ONLY,
    SHORT,
}

class FormatLongDurationMsAsSmallestHhMmSsStringUseCase(
    private val durationStringFormatterDigits: DurationFormatPatterns,
    private val durationStringFormatterShort: DurationFormatPatterns,
    private val logger: HiitLogger,
) {
    /**
     * format a Long duration in Milliseconds to a HMS String
     * no leading zero, zero smallest units are removed according to provided string formatters
     * examples of formatting:
     * value in MS -> return with provided string formats / return with default values
     * arguments all have default values leading to the basic Hh:Mm:Ss formatting
     * using the default formatting will not remove leading 0 and the zero smallest units
     * See unit tests for examples of formatting values and the expected result
     **/
    fun execute(
        durationMs: Long,
        formatStyle: DurationFormatStyle = DurationFormatStyle.DIGITS_ONLY,
    ): String {
        val durationStringFormatter =
            when (formatStyle) {
                DurationFormatStyle.DIGITS_ONLY -> durationStringFormatterDigits
                DurationFormatStyle.SHORT -> durationStringFormatterShort
            }
        val hours = TimeUnit.MILLISECONDS.toHours(durationMs)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60

        return when {
            // ex: 5s / 32s
            hours == 0L && minutes == 0L -> {
                durationStringFormatter.seconds.format(seconds)
            }
            //
            hours == 0L && minutes > 0L -> {
                when (seconds) {
                    // ex: 3mn / 13mn
                    0L -> {
                        runCatching {
                            durationStringFormatter.minutesNoSeconds.format(minutes)
                        }.getOrElse {
                            // the default String needs the 0 seconds argument to be formatted
                            durationStringFormatter.minutesNoSeconds.format(minutes, seconds)
                        }
                    }
                    // ex: 2mn04s / 1mn43s / 23mn52s
                    else -> {
                        durationStringFormatter.minutesSeconds.format(
                            minutes,
                            seconds,
                        )
                    }
                }
            }
            else -> {
                when (seconds) {
                    0L -> {
                        runCatching {
                            when (minutes) {
                                // ex: 1h / 23h
                                0L -> {
                                    durationStringFormatter.hoursNoMinutesNoSeconds.format(hours)
                                }
                                // ex: 2h03mn / 3h45mn
                                else -> {
                                    durationStringFormatter.hoursMinutesNoSeconds.format(
                                        hours,
                                        minutes,
                                    )
                                }
                            }
                        }.getOrElse {
                            // the default String needs the 0 minutes & 0 seconds arguments to be formatted
                            durationStringFormatter.hoursMinutesSeconds.format(
                                hours,
                                minutes,
                                seconds,
                            )
                        }
                    }
                    // ex: 3h04mn05s / 4h12mn34s / 12h34mn56s
                    else -> {
                        durationStringFormatter.hoursMinutesSeconds.format(
                            hours,
                            minutes,
                            seconds,
                        )
                    }
                }
            }
        }
    }
}
