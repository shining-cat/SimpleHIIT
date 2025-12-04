package fr.shiningcat.simplehiit.android.mobile.ui.session

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.Session
import fr.shiningcat.simplehiit.domain.common.models.SessionRecord
import fr.shiningcat.simplehiit.domain.common.models.SessionSettings
import fr.shiningcat.simplehiit.domain.common.models.StepTimerState
import fr.shiningcat.simplehiit.domain.common.usecases.DurationFormatStyle
import fr.shiningcat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shiningcat.simplehiit.domain.session.usecases.BuildSessionUseCase
import fr.shiningcat.simplehiit.domain.session.usecases.GetSessionSettingsUseCase
import fr.shiningcat.simplehiit.domain.session.usecases.InsertSessionUseCase
import fr.shiningcat.simplehiit.domain.session.usecases.StepTimerUseCase
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class SessionInteractorTest : AbstractMockkTest() {
    private val mockGetSessionSettingsUseCase = mockk<GetSessionSettingsUseCase>()
    private val mockBuildSessionUseCase = mockk<BuildSessionUseCase>()
    private val mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase =
        mockk<FormatLongDurationMsAsSmallestHhMmSsStringUseCase>()
    private val mockStepTimerUseCase = mockk<StepTimerUseCase>()
    private val mockInsertSessionUseCase = mockk<InsertSessionUseCase>()

    private val settingsFlow = MutableSharedFlow<Output<SessionSettings>>()
    private val mockSessionSettings = mockk<SessionSettings>()
    private val mockSession = mockk<Session>()
    private val testFormattedDuration = "formatted duration"
    private val stepTimerState = MutableStateFlow(StepTimerState())
    private val testReturnInt = 123
    private val mockSessionRecord = mockk<SessionRecord>()

    private val testedInteractor =
        SessionInteractorImpl(
            mockGetSessionSettingsUseCase,
            mockBuildSessionUseCase,
            mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase,
            mockStepTimerUseCase,
            mockInsertSessionUseCase,
        )

    @BeforeEach
    fun setUpMock() {
        coEvery { mockGetSessionSettingsUseCase.execute() } answers { settingsFlow }
        coEvery { mockBuildSessionUseCase.execute(any()) } returns mockSession
        coEvery {
            mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                durationMs = any(),
                formatStyle = any(),
            )
        } returns testFormattedDuration
        coEvery { mockStepTimerUseCase.start(any()) } just Runs
        coEvery { mockStepTimerUseCase.timerStateFlow } answers { stepTimerState }
        coEvery { mockInsertSessionUseCase.execute(any()) } returns Output.Success(testReturnInt)
    }

    @Test
    fun `calls on interactor getSessionSettings calls GetSessionSettingsUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            val result = testedInteractor.getSessionSettings()
            coVerify(exactly = 1) { mockGetSessionSettingsUseCase.execute() }
            assertEquals(settingsFlow, result)
        }

    @Test
    fun `calls on interactor buildSession calls BuildSessionUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            val result =
                testedInteractor.buildSession(mockSessionSettings)
            coVerify(exactly = 1) {
                mockBuildSessionUseCase.execute(
                    mockSessionSettings,
                )
            }
            assertEquals(mockSession, result)
        }

    @Test
    fun `calls on interactor formatLongDurationMsAsSmallestHhMmSsString calls FormatLongDurationMsAsSmallestHhMmSsStringUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            val result =
                testedInteractor.formatLongDurationMsAsSmallestHhMmSsString(
                    durationMs = 123L,
                )
            coVerify(exactly = 1) {
                mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                    durationMs = 123L,
                    formatStyle = DurationFormatStyle.SHORT,
                )
            }
            assertEquals(testFormattedDuration, result)
        }

    @Test
    fun `calls on interactor startStepTimer calls StepTimerUseCase start`() =
        runTest(UnconfinedTestDispatcher()) {
            testedInteractor.startStepTimer(567L)
            coVerify(exactly = 1) { mockStepTimerUseCase.start(567L) }
        }

    @Test
    fun `calls on interactor getStepTimerState calls StepTimerUseCase timerStateFlow`() =
        runTest(UnconfinedTestDispatcher()) {
            val result = testedInteractor.getStepTimerState()
            coVerify(exactly = 1) { mockStepTimerUseCase.timerStateFlow }
            assertEquals(stepTimerState, result)
        }

    @Test
    fun `calls on interactor insertSession calls InsertSessionUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            val result = testedInteractor.insertSession(mockSessionRecord)
            coVerify(exactly = 1) { mockInsertSessionUseCase.execute(mockSessionRecord) }
            assertTrue(result is Output.Success)
            result as Output.Success
            assertEquals(testReturnInt, result.result)
        }
}
