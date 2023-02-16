package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.utils.HiitLogger
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ResetAllSettingsUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val mockSimpleHiitLogger = mockk<HiitLogger>()
    private val testedUseCase = ResetAllSettingsUseCase(mockSimpleHiitRepository, mockHiitLogger)

    @Test
    fun `calls resetAllSettings repo`() = runTest {
        coEvery { mockSimpleHiitRepository.resetAllSettings() } just Runs
        //
        testedUseCase.execute()
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.resetAllSettings() }
    }

}