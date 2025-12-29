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
        single {
            ComposeExercisesListForSessionUseCase(
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            BuildSessionUseCase(
                composeExercisesListForSessionUseCase = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            GetSessionSettingsUseCase(
                usersRepository = get(),
                settingsRepository = get(),
                logger = get(),
            )
        }

        single {
            InsertSessionUseCase(
                sessionsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            StepTimerUseCase(
                timerDispatcher = get(named("TimerDispatcher")),
                timeProvider = get(),
                logger = get(),
            )
        }
    }
