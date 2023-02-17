package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import io.mockk.mockk

internal class CalculateCurrentStreakUseCaseTest : AbstractMockkTest() {

    private val mockConsecutiveDaysOrCloserUseCaseTest = mockk<ConsecutiveDaysOrCloserUseCase>()
    private val testedUseCase = CalculateCurrentStreakUseCase(
        mockConsecutiveDaysOrCloserUseCaseTest,
        mockHiitLogger
    )


}