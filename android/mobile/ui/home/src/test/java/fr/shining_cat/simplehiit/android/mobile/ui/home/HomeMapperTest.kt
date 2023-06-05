package fr.shining_cat.simplehiit.android.mobile.ui.home

import fr.shining_cat.simplehiit.commondomain.Constants
import fr.shining_cat.simplehiit.commondomain.Output
import fr.shining_cat.simplehiit.commondomain.models.DurationStringFormatter
import fr.shining_cat.simplehiit.commondomain.models.HomeSettings
import fr.shining_cat.simplehiit.commondomain.models.User
import fr.shining_cat.simplehiit.commondomain.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shining_cat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class HomeMapperTest : AbstractMockkTest() {

    private val mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase =
        mockk<FormatLongDurationMsAsSmallestHhMmSsStringUseCase>()
    private val testedMapper =
        fr.shining_cat.simplehiit.android.mobile.ui.home.HomeMapper(
            mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase,
            mockHiitLogger
        )

    @BeforeEach
    fun setUpMock() {
        coEvery {
            mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                any(),
                any()
            )
        } returns mockDurationString
    }

    @ParameterizedTest(name = "{index} -> called with {0} should return {1}")
    @MethodSource("homeSettingsArguments")
    fun `mapping homeSettings to correct viewstate`(
        input: Output<HomeSettings>,
        expectedOutput: fr.shining_cat.simplehiit.android.mobile.ui.home.HomeViewState
    ) {
        val result = testedMapper.map(
            homeSettingsOutput = input,
            durationStringFormatter = DurationStringFormatter()
        )
        //
        if (input is Output.Success) {
            coVerify(exactly = 1) {
                mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                    durationMs = input.result.cycleLengthMs,
                    durationStringFormatter = DurationStringFormatter()
                )
            }
        } else {
            coVerify(exactly = 0) {
                mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(any(), any())
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
        private const val mockDurationString = "This is a test duration string"

        @JvmStatic
        fun homeSettingsArguments() =
            Stream.of(
                Arguments.of(
                    Output.Success(
                        HomeSettings(
                            numberCumulatedCycles = 3,
                            cycleLengthMs = 123L,
                            users = listOf(testUser1, testUser3, testUser2, testUser4)
                        )
                    ),
                    fr.shining_cat.simplehiit.android.mobile.ui.home.HomeViewState.Nominal(
                        numberCumulatedCycles = 3,
                        cycleLength = mockDurationString,
                        users = listOf(testUser1, testUser3, testUser2, testUser4)
                    )
                ),
                Arguments.of(
                    Output.Success(
                        HomeSettings(
                            numberCumulatedCycles = 5,
                            cycleLengthMs = 234L,
                            users = listOf(testUser1, testUser2)
                        )
                    ),
                    fr.shining_cat.simplehiit.android.mobile.ui.home.HomeViewState.Nominal(
                        numberCumulatedCycles = 5,
                        cycleLength = mockDurationString,
                        users = listOf(testUser1, testUser2)
                    )
                ),
                Arguments.of(
                    Output.Success(
                        HomeSettings(
                            numberCumulatedCycles = 3,
                            cycleLengthMs = 456L,
                            users = listOf()
                        )
                    ),
                    fr.shining_cat.simplehiit.android.mobile.ui.home.HomeViewState.MissingUsers(
                        numberCumulatedCycles = 3,
                        cycleLength = mockDurationString
                    )
                ),
                Arguments.of(
                    Output.Error(
                        errorCode = Constants.Errors.NO_USERS_FOUND,
                        exception = testException
                    ),
                    fr.shining_cat.simplehiit.android.mobile.ui.home.HomeViewState.Error(Constants.Errors.NO_USERS_FOUND.code)
                ),
                Arguments.of(
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_FETCH_FAILED,
                        exception = testException
                    ),
                    fr.shining_cat.simplehiit.android.mobile.ui.home.HomeViewState.Error(Constants.Errors.DATABASE_FETCH_FAILED.code)
                )
            )
    }
}