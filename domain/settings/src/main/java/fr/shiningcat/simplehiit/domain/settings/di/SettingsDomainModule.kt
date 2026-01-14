@file:KeepForKoin

package fr.shiningcat.simplehiit.domain.settings.di

import fr.shiningcat.simplehiit.commonutils.annotations.KeepForKoin
import fr.shiningcat.simplehiit.domain.settings.usecases.CheckIfAnotherUserUsesThatNameUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.CreateUserUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.DeleteUserUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.GetCurrentAppLanguageUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.GetGeneralSettingsUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ResetAllSettingsUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SaveSelectedExerciseTypesUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetAppLanguageUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetAppThemeUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetBeepSoundUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetNumberOfWorkPeriodsUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetPeriodStartCountDownUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetRestPeriodLengthUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetSessionStartCountDownUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetWorkPeriodLengthUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ToggleExerciseTypeInListUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.UpdateUserNameUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ValidateInputPeriodStartCountdownUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ValidateInputSessionStartCountdownUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ValidateInputUserNameUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ValidateNumberOfWorkPeriodsUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ValidatePeriodLengthUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val settingsDomainModule =
    module {
        // UseCases
        factory {
            CheckIfAnotherUserUsesThatNameUseCase(
                usersRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            CreateUserUseCase(
                usersRepository = get(),
                checkIfAnotherUserUsesThatNameUseCase = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            DeleteUserUseCase(
                usersRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            GetCurrentAppLanguageUseCase(
                languageRepository = get(),
            )
        }

        factory {
            GetGeneralSettingsUseCase(
                usersRepository = get(),
                settingsRepository = get(),
                getCurrentAppLanguageUseCase = get(),
                logger = get(),
            )
        }

        factory {
            ResetAllSettingsUseCase(
                settingsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            SaveSelectedExerciseTypesUseCase(
                settingsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            SetAppLanguageUseCase(
                languageRepository = get(),
                logger = get(),
            )
        }

        factory {
            SetAppThemeUseCase(
                settingsRepository = get(),
            )
        }

        factory {
            SetBeepSoundUseCase(
                settingsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            SetNumberOfWorkPeriodsUseCase(
                settingsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            SetPeriodStartCountDownUseCase(
                settingsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            SetRestPeriodLengthUseCase(
                settingsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            SetSessionStartCountDownUseCase(
                settingsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            SetWorkPeriodLengthUseCase(
                settingsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            ToggleExerciseTypeInListUseCase(
                logger = get(),
            )
        }

        factory {
            UpdateUserNameUseCase(
                usersRepository = get(),
                checkIfAnotherUserUsesThatNameUseCase = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            ValidateInputPeriodStartCountdownUseCase(
                logger = get(),
            )
        }

        factory {
            ValidateInputSessionStartCountdownUseCase(
                logger = get(),
            )
        }

        factory {
            ValidateInputUserNameUseCase(
                logger = get(),
            )
        }

        factory {
            ValidateNumberOfWorkPeriodsUseCase(
                logger = get(),
            )
        }

        factory {
            ValidatePeriodLengthUseCase(
                logger = get(),
            )
        }
    }
