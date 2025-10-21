package fr.shiningcat.simplehiit.domain.common.usecases

import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import fr.shiningcat.simplehiit.domain.common.datainterfaces.UsersRepository
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
internal class ResetWholeAppUseCaseTest : AbstractMockkTest() {
    private val mockSettingsRepository = mockk<SettingsRepository>()
    private val mockUsersRepository = mockk<UsersRepository>()

    @Test
    fun `calls resetAllSettings repo`() =
        runTest {
            val testedUseCase =
                ResetWholeAppUseCase(
                    settingsRepository = mockSettingsRepository,
                    usersRepository = mockUsersRepository,
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    simpleHiitLogger = mockHiitLogger,
                )
            coEvery { mockSettingsRepository.resetAllSettings() } just Runs
            coEvery { mockUsersRepository.deleteAllUsers() } just Runs
            //
            testedUseCase.execute()
            //
            coVerify(exactly = 1) { mockSettingsRepository.resetAllSettings() }
            coVerify(exactly = 1) { mockUsersRepository.deleteAllUsers() }
        }
}
