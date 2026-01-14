@file:KeepForKoin

package fr.shiningcat.simplehiit.sharedui.home.di

import fr.shiningcat.simplehiit.commonutils.annotations.KeepForKoin
import fr.shiningcat.simplehiit.sharedui.home.HomeInteractor
import fr.shiningcat.simplehiit.sharedui.home.HomeInteractorImpl
import fr.shiningcat.simplehiit.sharedui.home.HomePresenter
import fr.shiningcat.simplehiit.sharedui.home.HomeViewStateMapper
import org.koin.dsl.module

val homeModule =
    module {
        // ViewStateMapper - factory since it was ViewModelComponent scoped
        factory {
            HomeViewStateMapper(
                formatLongDurationMsAsSmallestHhMmSsStringUseCase = get(),
                logger = get(),
            )
        }

        // Interactor - factory since it was ViewModelComponent scoped
        factory<HomeInteractor> {
            HomeInteractorImpl(
                getHomeSettingsUseCase = get(),
                setTotalRepetitionsNumberUseCase = get(),
                toggleUserSelectedUseCase = get(),
                resetWholeAppUseCase = get(),
                validateInputNumberCyclesUseCase = get(),
            )
        }

        // Presenter - factory scoped to ViewModel lifecycle
        factory {
            HomePresenter(
                homeInteractor = get(),
                homeViewStateMapper = get(),
                modifyNumberCyclesUseCase = get(),
                logger = get(),
            )
        }
    }
