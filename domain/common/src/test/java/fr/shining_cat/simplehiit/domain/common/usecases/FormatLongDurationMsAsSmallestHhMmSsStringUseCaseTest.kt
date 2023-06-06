package fr.shining_cat.simplehiit.domain.common.usecases

import fr.shining_cat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shining_cat.simplehiit.testutils.AbstractMockkTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class FormatLongDurationMsAsSmallestHhMmSsStringUseCaseTest : AbstractMockkTest() {

    private val testedUseCase = FormatLongDurationMsAsSmallestHhMmSsStringUseCase(mockHiitLogger)

    @ParameterizedTest(name = "{index} -> {0} should become {1}")
    @MethodSource("convertLongToStringArguments")
    fun `returns correctly formatted string with default formatting options`(
        inputDurationMs: Long,
        expectedOutput: String
    ) {
        val result = testedUseCase.execute(inputDurationMs, DurationStringFormatter())
        assertEquals(expectedOutput, result)
    }

    @ParameterizedTest(name = "{index} -> {0} should become {1}")
    @MethodSource("convertLongToStringArgumentsSpecificFormats")
    fun `returns correctly formatted string with custom formatting options`(
        inputDurationMs: Long,
        expectedOutput: String
    ) {
        val durationsFormatter = DurationStringFormatter(
            hoursMinutesSeconds = "%1\$dh %2$02dmn %3$02ds",
            hoursMinutesNoSeconds = "%1\$dh %2$02dmn",
            hoursNoMinutesNoSeconds = "%1\$dh",
            minutesSeconds = "%1\$dmn %2\$02ds",
            minutesNoSeconds = "%1\$dmn",
            seconds = "%ds"
        )
        val result = testedUseCase.execute(
            inputDurationMs,
            durationsFormatter
        )
        assertEquals(expectedOutput, result)
    }

    ////////////////////////
    private companion object {

        @JvmStatic
        fun convertLongToStringArguments() =
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
                Arguments.of(124000L, "2mn 04s"),
                Arguments.of(103000L, "1mn 43s"),
                Arguments.of(3600000L, "1h"),
                Arguments.of(57600000L, "16h"),
                Arguments.of(46825000L, "13h 00mn 25s"),
                Arguments.of(7380000L, "2h 03mn"),
                Arguments.of(13500000L, "3h 45mn"),
                Arguments.of(11045000L, "3h 04mn 05s"),
                Arguments.of(443096000L, "123h 04mn 56s")
            )
    }

}