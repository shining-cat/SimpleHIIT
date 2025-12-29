package fr.shiningcat.simplehiit.sharedui.home.di

import fr.shiningcat.simplehiit.sharedui.home.HomeInteractor
import fr.shiningcat.simplehiit.sharedui.home.HomeInteractorImpl
import fr.shiningcat.simplehiit.sharedui.home.HomeViewModel
import fr.shiningcat.simplehiit.sharedui.home.HomeViewStateMapper
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
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

        // ViewModel
        viewModel {
            HomeViewModel(
                homeInteractor = get(),
                homeViewStateMapper = get(),
                mainDispatcher = get(named("MainDispatcher")),
                logger = get(),
            )
        }
    }
