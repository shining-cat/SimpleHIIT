package fr.shining_cat.simplehiit.domain.settings

import fr.shining_cat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.testutils.AbstractMockkTest
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@OptIn(ExperimentalCoroutinesApi::class)
internal class SetNumberOfWorkPeriodUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()

    @ParameterizedTest(name = "{index} -> when called with {0}, should call SimpleHiitRepository with {0}")
    @ValueSource(ints = [7, 9, 27])
    fun `calls repo with corresponding value and returns repo success`(
        testValue: Int
    ) = runTest {
        val testedUseCase = fr.shining_cat.simplehiit.domain.settings.SetNumberOfWorkPeriodsUseCase(
            simpleHiitRepository = mockSimpleHiitRepository,
            defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
            simpleHiitLogger = mockHiitLogger
        )
        coEvery { mockSimpleHiitRepository.setNumberOfWorkPeriods(any()) } just Runs
        //
        testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.setNumberOfWorkPeriods(testValue) }
    }

}