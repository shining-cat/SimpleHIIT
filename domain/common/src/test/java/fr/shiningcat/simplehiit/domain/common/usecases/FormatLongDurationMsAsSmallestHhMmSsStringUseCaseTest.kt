package fr.shiningcat.simplehiit.domain.common.usecases

import fr.shiningcat.simplehiit.domain.common.DurationStringFormatter
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class FormatLongDurationMsAsSmallestHhMmSsStringUseCaseTest : AbstractMockkTest() {
    private val durationsFormatterShort =
        DurationStringFormatter(
            hoursMinutesSeconds = "%1\$dh\u00A0%2$02dmn\u00A0%3$02ds",
            hoursMinutesNoSeconds = "%1\$dh\u00A0%2$02dmn",
            hoursNoMinutesNoSeconds = "%1\$dh",
            minutesSeconds = "%1\$dmn\u00A0%2\$02ds",
            minutesNoSeconds = "%1\$dmn",
            seconds = "%ds",
        )
    private val durationsFormatterDigits =
        DurationStringFormatter(
            hoursMinutesSeconds = "%1$02d:%2$02d:%3$02d",
            hoursMinutesNoSeconds = "%1$02d:%2$02d:%3$02d",
            hoursNoMinutesNoSeconds = "%1$02d:%2$02d:%3$02d",
            minutesSeconds = "%1$02d:%2$02d",
            minutesNoSeconds = "%1$02d:%2$02d",
            seconds = "%02d",
        )
    private val testedUseCase =
        FormatLongDurationMsAsSmallestHhMmSsStringUseCase(
            durationStringFormatterDigits = durationsFormatterDigits,
            durationStringFormatterShort = durationsFormatterShort,
            hiitLogger = mockHiitLogger,
        )

    @ParameterizedTest(name = "{index} -> {0} should become {1}")
    @MethodSource("convertLongToStringArguments")
    fun `returns correctly formatted string with default formatting options`(
        inputDurationMs: Long,
        expectedOutput: String,
    ) {
        val result = testedUseCase.execute(durationMs = inputDurationMs)
        assertEquals(expectedOutput, result)
    }

    @ParameterizedTest(name = "{index} -> {0} should become {1}")
    @MethodSource("convertLongToStringArguments")
    fun `returns correctly formatted string with digits formatting options`(
        inputDurationMs: Long,
        expectedOutput: String,
    ) {
        val result =
            testedUseCase.execute(
                durationMs = inputDurationMs,
                formatStyle = DurationFormatStyle.DIGITS_ONLY,
            )
        assertEquals(expectedOutput, result)
    }

    @ParameterizedTest(name = "{index} -> {0} should become {1}")
    @MethodSource("convertLongToStringArgumentsSpecificFormats")
    fun `returns correctly formatted string with custom formatting options`(
        inputDurationMs: Long,
        expectedOutput: String,
    ) {
        val result =
            testedUseCase.execute(
                durationMs = inputDurationMs,
                formatStyle = DurationFormatStyle.SHORT,
            )
        assertEquals(expectedOutput, result)
    }

    // //////////////////////
    private companion object {
        @JvmStatic
        fun convertLongToStringArguments(): Stream<Arguments> =
            Stream.of(
                Arguments.of(5000L, "05"),
                Arguments.of(32000L, "32"),
                Arguments.of(180000L, "03:00"),
                Arguments.of(780000L, "13:00"),
                Arguments.of(124000L, "02:04"),
                Arguments.of(103000L, "01:43"),
                Arguments.of(3600000L, "01:00:00"),
                Arguments.of(57600000L, "16:00:00"),
                Arguments.of(46825000L, "13:00:25"),
                Arguments.of(7380000L, "02:03:00"),
                Arguments.of(13500000L, "03:45:00"),
                Arguments.of(11045000L, "03:04:05"),
                Arguments.of(443096000L, "123:04:56"),
            )

        @JvmStatic
        fun convertLongToStringArgumentsSpecificFormats() =
            Stream.of(
                Arguments.of(5000L, "5s"),
                Arguments.of(32000L, "32s"),
                Arguments.of(180000L, "3mn"),
                Arguments.of(780000L, "13mn"),
                Arguments.of(124000L, "2mn\u00A004s"),
                Arguments.of(103000L, "1mn\u00A043s"),
                Arguments.of(3600000L, "1h"),
                Arguments.of(57600000L, "16h"),
                Arguments.of(46825000L, "13h\u00A000mn\u00A025s"),
                Arguments.of(7380000L, "2h\u00A003mn"),
                Arguments.of(13500000L, "3h\u00A045mn"),
                Arguments.of(11045000L, "3h\u00A004mn\u00A005s"),
                Arguments.of(443096000L, "123h\u00A004mn\u00A056s"),
            )
    }
}
