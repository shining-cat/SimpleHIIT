package fr.shining_cat.simplehiit.commondomain.usecases

import fr.shining_cat.simplehiit.commondomain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.testutils.AbstractMockkTest
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ResetWholeAppUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()

    @Test
    fun `calls resetAllSettings repo`() = runTest {
        val testedUseCase = ResetWholeAppUseCase(
            simpleHiitRepository = mockSimpleHiitRepository,
            defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
            simpleHiitLogger = mockHiitLogger
        )
        coEvery { mockSimpleHiitRepository.resetAllSettings() } just Runs
        coEvery { mockSimpleHiitRepository.deleteAllUsers() } just Runs
        //
        testedUseCase.execute()
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.resetAllSettings() }
        coVerify(exactly = 1) { mockSimpleHiitRepository.deleteAllUsers() }
    }

}