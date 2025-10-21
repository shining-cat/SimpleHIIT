package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@OptIn(ExperimentalCoroutinesApi::class)
internal class SetNumberOfWorkPeriodUseCaseTest : AbstractMockkTest() {
    private val mockSettingsRepository = mockk<SettingsRepository>()

    @ParameterizedTest(name = "{index} -> when called with {0}, should call SimpleHiitRepository with {0}")
    @ValueSource(ints = [7, 9, 27])
    fun `calls repo with corresponding value and returns repo success`(testValue: Int) =
        runTest {
            val testedUseCase =
                SetNumberOfWorkPeriodsUseCase(
                    settingsRepository = mockSettingsRepository,
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    simpleHiitLogger = mockHiitLogger,
                )
            coEvery { mockSettingsRepository.setNumberOfWorkPeriods(any()) } just Runs
            //
            testedUseCase.execute(testValue)
            //
            coVerify(exactly = 1) { mockSettingsRepository.setNumberOfWorkPeriods(testValue) }
        }
}
