package fr.shiningcat.simplehiit.android.tv.ui.home

import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.HomeSettings
import fr.shiningcat.simplehiit.domain.common.models.LaunchSessionWarning
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.domain.common.usecases.DurationFormatStyle
import fr.shiningcat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class HomeViewStateMapperTest : AbstractMockkTest() {
    private val mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase =
        mockk<FormatLongDurationMsAsSmallestHhMmSsStringUseCase>()
    private val testedMapper =
        HomeViewStateMapper(
            mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase,
            mockHiitLogger,
        )

    @BeforeEach
    fun setUpMock() {
        coEvery {
            mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                durationMs = any(),
                formatStyle = any(),
            )
        } returns MOCK_DURATION_STRING
    }

    @ParameterizedTest(name = "{index} -> called with {0} should return {1}")
    @MethodSource("homeSettingsArguments")
    fun `mapping homeSettings to correct viewstate`(
        input: Output<HomeSettings>,
        expectedOutput: HomeViewState,
    ) {
        val result =
            testedMapper.map(
                homeSettingsOutput = input,
            )
        //
        if (input is Output.Success) {
            coVerify(exactly = 1) {
                mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                    durationMs = input.result.cycleLengthMs,
                    formatStyle = DurationFormatStyle.SHORT,
                )
            }
            coVerify(exactly = 1) {
                mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                    durationMs = (input.result.cycleLengthMs.times(input.result.numberCumulatedCycles)),
                    formatStyle = DurationFormatStyle.SHORT,
                )
            }
        } else {
            coVerify(exactly = 0) {
                mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                    durationMs = any(),
                    formatStyle = any(),
                )
            }
        }
        assertEquals(expectedOutput, result)
    }

    private companion object {
        private val testUser1 = User(id = 123L, name = "test user 1 name", selected = true)
        private val testUser2 = User(id = 234L, name = "test user 2 name", selected = false)
        private val testUser3 = User(id = 345L, name = "test user 3 name", selected = true)
        private val testUser4 = User(id = 456L, name = "test user 4 name", selected = false)
        private val testException = Exception("this is a test exception")
        private const val MOCK_DURATION_STRING = "This is a test duration string"

        @JvmStatic
        fun homeSettingsArguments() =
            Stream.of(
                Arguments.of(
                    Output.Success(
                        HomeSettings(
                            numberCumulatedCycles = 3,
                            cycleLengthMs = 123L,
                            users = listOf(testUser1, testUser3, testUser2, testUser4),
                        ),
                    ),
                    HomeViewState.Nominal(
                        numberCumulatedCycles = 3,
                        cycleLength = MOCK_DURATION_STRING,
                        users = listOf(testUser1, testUser3, testUser2, testUser4),
                        totalSessionLengthFormatted = MOCK_DURATION_STRING,
                        warning = null,
                    ),
                ),
                Arguments.of(
                    Output.Success(
                        HomeSettings(
                            numberCumulatedCycles = 5,
                            cycleLengthMs = 234L,
                            users = listOf(testUser1, testUser2),
                        ),
                    ),
                    HomeViewState.Nominal(
                        numberCumulatedCycles = 5,
                        cycleLength = MOCK_DURATION_STRING,
                        users = listOf(testUser1, testUser2),
                        totalSessionLengthFormatted = MOCK_DURATION_STRING,
                        warning = null,
                    ),
                ),
                Arguments.of(
                    Output.Success(
                        HomeSettings(
                            numberCumulatedCycles = 7,
                            cycleLengthMs = 345L,
                            users = listOf(testUser1, testUser2, testUser3),
                            warning = LaunchSessionWarning.DUPLICATED_EXERCISES,
                        ),
                    ),
                    HomeViewState.Nominal(
                        numberCumulatedCycles = 7,
                        cycleLength = MOCK_DURATION_STRING,
                        users = listOf(testUser1, testUser2, testUser3),
                        totalSessionLengthFormatted = MOCK_DURATION_STRING,
                        warning = LaunchSessionWarning.DUPLICATED_EXERCISES,
                    ),
                ),
                Arguments.of(
                    Output.Success(
                        HomeSettings(
                            numberCumulatedCycles = 3,
                            cycleLengthMs = 456L,
                            users = listOf(),
                        ),
                    ),
                    HomeViewState.MissingUsers(
                        numberCumulatedCycles = 3,
                        cycleLength = MOCK_DURATION_STRING,
                        totalSessionLengthFormatted = MOCK_DURATION_STRING,
                    ),
                ),
                Arguments.of(
                    Output.Error(
                        errorCode = Constants.Errors.NO_USERS_FOUND,
                        exception = testException,
                    ),
                    HomeViewState.Error(Constants.Errors.NO_USERS_FOUND.code),
                ),
                Arguments.of(
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_FETCH_FAILED,
                        exception = testException,
                    ),
                    HomeViewState.Error(Constants.Errors.DATABASE_FETCH_FAILED.code),
                ),
            )
    }
}
