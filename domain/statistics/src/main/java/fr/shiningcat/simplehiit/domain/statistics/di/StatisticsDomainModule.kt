/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
@file:KeepForKoin

package fr.shiningcat.simplehiit.domain.statistics.di

import fr.shiningcat.simplehiit.commonutils.annotations.KeepForKoin
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
        factory {
            ConsecutiveDaysOrCloserUseCase(
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            CalculateAverageSessionsPerWeekUseCase(
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            CalculateCurrentStreakUseCase(
                consecutiveDaysOrCloserUseCase = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            CalculateLongestStreakUseCase(
                consecutiveDaysOrCloserUseCase = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            DeleteSessionsForUserUseCase(
                sessionsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                simpleHiitLogger = get(),
            )
        }

        factory {
            GetAllUsersUseCase(
                usersRepository = get(),
                logger = get(),
            )
        }

        factory {
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
