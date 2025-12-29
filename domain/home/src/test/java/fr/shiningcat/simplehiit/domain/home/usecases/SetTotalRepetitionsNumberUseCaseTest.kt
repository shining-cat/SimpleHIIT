package fr.shiningcat.simplehiit.domain.home.usecases

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
internal class SetTotalRepetitionsNumberUseCaseTest : AbstractMockkTest() {
    private val mockSettingsRepository = mockk<SettingsRepository>()

    @ParameterizedTest(name = "{index} -> when called with {0}, should call SimpleHiitRepository with {0}")
    @ValueSource(ints = [1, 4, 53])
    fun `calls repo with corresponding value and returns repo success`(testValue: Int) =
        runTest {
            val testedUseCase =
                SetTotalRepetitionsNumberUseCase(
                    settingsRepository = mockSettingsRepository,
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    logger = mockHiitLogger,
                )
            coEvery { mockSettingsRepository.setTotalRepetitionsNumber(any()) } just Runs
            //
            testedUseCase.execute(testValue)
            //
            coVerify(exactly = 1) { mockSettingsRepository.setTotalRepetitionsNumber(testValue) }
        }
}
