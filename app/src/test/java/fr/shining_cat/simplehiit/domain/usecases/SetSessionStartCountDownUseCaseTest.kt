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
internal class SetSessionStartCountDownUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val testedUseCase = SetSessionStartCountDownUseCase(mockSimpleHiitRepository, mockHiitLogger)

    @ParameterizedTest(name = "{index} -> when called with {0}, should call SimpleHiitRepository with {0}")
    @ValueSource(longs = [8000L, 3000L, 13000L])
    fun `calls repo with corresponding value and returns repo success`(
        testValue: Long
    ) = runTest {
        coEvery { mockSimpleHiitRepository.setSessionStartCountdown(any()) } just Runs
        //
        testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.setSessionStartCountdown(testValue) }
    }

}