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
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ResetAllSettingsUseCaseTest : AbstractMockkTest() {
    private val mockSettingsRepository = mockk<SettingsRepository>()

    @Test
    fun `calls resetAllSettings repo`() =
        runTest {
            val testedUseCase =
                ResetAllSettingsUseCase(
                    settingsRepository = mockSettingsRepository,
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    simpleHiitLogger = mockHiitLogger,
                )
            coEvery { mockSettingsRepository.resetAllSettings() } just Runs
            //
            testedUseCase.execute()
            //
            coVerify(exactly = 1) { mockSettingsRepository.resetAllSettings() }
        }
}
