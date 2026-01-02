package fr.shiningcat.simplehiit.domain.home.di

import fr.shiningcat.simplehiit.domain.home.usecases.DetectSessionWarningUseCase
import fr.shiningcat.simplehiit.domain.home.usecases.GetHomeSettingsUseCase
import fr.shiningcat.simplehiit.domain.home.usecases.ModifyNumberCyclesUseCase
import fr.shiningcat.simplehiit.domain.home.usecases.SetTotalRepetitionsNumberUseCase
import fr.shiningcat.simplehiit.domain.home.usecases.ToggleUserSelectedUseCase
import fr.shiningcat.simplehiit.domain.home.usecases.ValidateInputNumberCyclesUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val homeDomainModule =
    module {
        // UseCases
        factory { DetectSessionWarningUseCase() }

        factory {
            GetHomeSettingsUseCase(
                usersRepository = get(),
                settingsRepository = get(),
                detectSessionWarningUseCase = get(),
                logger = get(),
            )
        }

        factory {
            SetTotalRepetitionsNumberUseCase(
                settingsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            ToggleUserSelectedUseCase(
                usersRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            ValidateInputNumberCyclesUseCase(
                logger = get(),
            )
        }

        factory {
            ModifyNumberCyclesUseCase(
                logger = get(),
            )
        }
    }
