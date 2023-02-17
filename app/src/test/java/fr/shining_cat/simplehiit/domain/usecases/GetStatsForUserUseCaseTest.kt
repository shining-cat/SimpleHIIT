package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*

internal class GetStatsForUserUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val mockCalculateCurrentStreakUseCase = mockk<CalculateCurrentStreakUseCase>()
    private val mockCalculateLongestStreakUseCase = mockk<CalculateLongestStreakUseCase>()
    private val mockCalculateAverageSessionsPerWeekUseCase = mockk<CalculateAverageSessionsPerWeekUseCase>()

    private val testedUseCase = GetStatsForUserUseCase(
        simpleHiitRepository = mockSimpleHiitRepository,
        calculateCurrentStreakUseCase = mockCalculateCurrentStreakUseCase,
        calculateLongestStreakUseCase = mockCalculateLongestStreakUseCase,
        calculateAverageSessionsPerWeekUseCase = mockCalculateAverageSessionsPerWeekUseCase,
        simpleHiitLogger = mockHiitLogger
    )

    //TODO: write tests

}