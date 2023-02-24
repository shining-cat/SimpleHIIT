package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.utils.HiitLogger
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@OptIn(ExperimentalCoroutinesApi::class)
internal class SetPeriodStartCountDownUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val testedUseCase = SetPeriodStartCountDownUseCase(mockSimpleHiitRepository, mockHiitLogger)

    @ParameterizedTest(name = "{index} -> when called with {0}, should call SimpleHiitRepository with {0}")
    @ValueSource(longs = [5000L, 7000L, 15000L])
    fun `calls repo with corresponding value and returns repo success`(
        testValue: Long
    ) = runTest {
        coEvery { mockSimpleHiitRepository.setPeriodStartCountdown(any()) } just Runs
        //
        testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.setPeriodStartCountdown(testValue) }
    }

}