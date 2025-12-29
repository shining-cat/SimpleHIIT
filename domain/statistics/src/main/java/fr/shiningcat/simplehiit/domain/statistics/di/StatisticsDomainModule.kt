package fr.shiningcat.simplehiit.domain.statistics.di

import fr.shiningcat.simplehiit.domain.statistics.usecases.CalculateAverageSessionsPerWeekUseCase
import fr.shiningcat.simplehiit.domain.statistics.usecases.CalculateCurrentStreakUseCase
import fr.shiningcat.simplehiit.domain.statistics.usecases.CalculateLongestStreakUseCase
import fr.shiningcat.simplehiit.domain.statistics.usecases.ConsecutiveDaysOrCloserUseCase
import fr.shiningcat.simplehiit.domain.statistics.usecases.DeleteSessionsForUserUseCase
import fr.shiningcat.simplehiit.domain.statistics.usecases.GetAllUsersUseCase
import fr.shiningcat.simplehiit.domain.statistics.usecases.GetStatsForUserUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val statisticsDomainModule =
    module {
        // UseCases
        single {
            ConsecutiveDaysOrCloserUseCase(
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            CalculateAverageSessionsPerWeekUseCase(
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            CalculateCurrentStreakUseCase(
                consecutiveDaysOrCloserUseCase = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            CalculateLongestStreakUseCase(
                consecutiveDaysOrCloserUseCase = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            DeleteSessionsForUserUseCase(
                sessionsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                simpleHiitLogger = get(),
            )
        }

        single {
            GetAllUsersUseCase(
                usersRepository = get(),
                logger = get(),
            )
        }

        single {
            GetStatsForUserUseCase(
                sessionsRepository = get(),
                calculateCurrentStreakUseCase = get(),
                calculateLongestStreakUseCase = get(),
                calculateAverageSessionsPerWeekUseCase = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }
    }
