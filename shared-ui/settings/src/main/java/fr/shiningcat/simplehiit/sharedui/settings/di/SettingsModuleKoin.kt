@file:KeepForKoin

package fr.shiningcat.simplehiit.sharedui.settings.di

import fr.shiningcat.simplehiit.commonutils.annotations.KeepForKoin
import fr.shiningcat.simplehiit.sharedui.settings.SettingsInteractor
import fr.shiningcat.simplehiit.sharedui.settings.SettingsInteractorImpl
import fr.shiningcat.simplehiit.sharedui.settings.SettingsPresenter
import fr.shiningcat.simplehiit.sharedui.settings.SettingsViewStateMapper
import org.koin.core.qualifier.named
import org.koin.dsl.module

val settingsModule =
    module {
        // ViewStateMapper - factory since it was ViewModelComponent scoped
        factory {
            SettingsViewStateMapper(
                formatLongDurationMsAsSmallestHhMmSsStringUseCase = get(),
                logger = get(),
            )
        }

        // Interactor - factory since it was ViewModelComponent scoped
        factory<SettingsInteractor> {
            SettingsInteractorImpl(
                getGeneralSettingsUseCase = get(),
                setWorkPeriodLengthUseCase = get(),
                setRestPeriodLengthUseCase = get(),
                setNumberOfWorkPeriodsUseCase = get(),
                setBeepSoundUseCase = get(),
                setSessionStartCountDownUseCase = get(),
                setPeriodStartCountDownUseCase = get(),
                updateUserNameUseCase = get(),
                deleteUserUseCase = get(),
                createUserUseCase = get(),
                saveSelectedExerciseTypesUseCase = get(),
                setAppLanguageUseCase = get(),
                setAppThemeUseCase = get(),
                resetAllSettingsUseCase = get(),
                validatePeriodLengthUseCase = get(),
                validateNumberOfWorkPeriodsUseCase = get(),
                validateInputSessionStartCountdownUseCase = get(),
                validateInputPeriodStartCountdownUseCase = get(),
                validateInputUserNameUseCase = get(),
                toggleExerciseTypeInListUseCase = get(),
            )
        }

        // Presenter - factory scoped to ViewModel lifecycle
        factory {
            SettingsPresenter(
                settingsInteractor = get(),
                mapper = get(),
                dispatcher = get(named("MainDispatcher")),
                logger = get(),
            )
        }
    }
