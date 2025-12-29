package fr.shiningcat.simplehiit.domain.settings.di

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
        single {
            CheckIfAnotherUserUsesThatNameUseCase(
                usersRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            CreateUserUseCase(
                usersRepository = get(),
                checkIfAnotherUserUsesThatNameUseCase = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            DeleteUserUseCase(
                usersRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            GetCurrentAppLanguageUseCase(
                languageRepository = get(),
            )
        }

        single {
            GetGeneralSettingsUseCase(
                usersRepository = get(),
                settingsRepository = get(),
                getCurrentAppLanguageUseCase = get(),
                logger = get(),
            )
        }

        single {
            ResetAllSettingsUseCase(
                settingsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            SaveSelectedExerciseTypesUseCase(
                settingsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            SetAppLanguageUseCase(
                languageRepository = get(),
                logger = get(),
            )
        }

        single {
            SetAppThemeUseCase(
                settingsRepository = get(),
            )
        }

        single {
            SetBeepSoundUseCase(
                settingsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            SetNumberOfWorkPeriodsUseCase(
                settingsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            SetPeriodStartCountDownUseCase(
                settingsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            SetRestPeriodLengthUseCase(
                settingsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            SetSessionStartCountDownUseCase(
                settingsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            SetWorkPeriodLengthUseCase(
                settingsRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            ToggleExerciseTypeInListUseCase(
                logger = get(),
            )
        }

        single {
            UpdateUserNameUseCase(
                usersRepository = get(),
                checkIfAnotherUserUsesThatNameUseCase = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            ValidateInputPeriodStartCountdownUseCase(
                logger = get(),
            )
        }

        single {
            ValidateInputSessionStartCountdownUseCase(
                logger = get(),
            )
        }

        single {
            ValidateInputUserNameUseCase(
                logger = get(),
            )
        }

        single {
            ValidateNumberOfWorkPeriodsUseCase(
                logger = get(),
            )
        }

        single {
            ValidatePeriodLengthUseCase(
                logger = get(),
            )
        }
    }
