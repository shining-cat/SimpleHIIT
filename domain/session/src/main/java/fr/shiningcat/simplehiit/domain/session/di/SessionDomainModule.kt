package fr.shiningcat.simplehiit.domain.session.di

import fr.shiningcat.simplehiit.domain.session.usecases.BuildSessionUseCase
import fr.shiningcat.simplehiit.domain.session.usecases.ComposeExercisesListForSessionUseCase
import fr.shiningcat.simplehiit.domain.session.usecases.GetSessionSettingsUseCase
import fr.shiningcat.simplehiit.domain.session.usecases.InsertSessionUseCase
import fr.shiningcat.simplehiit.domain.session.usecases.StepTimerUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sessionDomainModule =
    module {
        // UseCases
        factory {
            ComposeExercisesListForSessionUseCase(
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            BuildSessionUseCase(
                composeExercisesListForSessionUseCase = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            GetSessionSettingsUseCase(
                usersRepository = get(),
                settingsRepository = get(),
                logger = get(),
            )
        }

        factory {
            InsertSessionUseCase(
                sessionsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            StepTimerUseCase(
                timerDispatcher = get(named("TimerDispatcher")),
                timeProvider = get(),
                logger = get(),
            )
        }
    }
