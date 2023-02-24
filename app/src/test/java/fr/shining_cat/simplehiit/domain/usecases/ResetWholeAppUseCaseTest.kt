package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ResetWholeAppUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val testedUseCase = ResetWholeAppUseCase(mockSimpleHiitRepository, mockHiitLogger)

    @Test
    fun `calls resetAllSettings repo`() = runTest {
        coEvery { mockSimpleHiitRepository.resetAllSettings() } just Runs
        coEvery { mockSimpleHiitRepository.deleteAllUsers() } just Runs
        //
        testedUseCase.execute()
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.resetAllSettings() }
        coVerify(exactly = 1) { mockSimpleHiitRepository.deleteAllUsers() }
    }

}