package fr.shiningcat.simplehiit.sharedui.statistics.di

import fr.shiningcat.simplehiit.sharedui.statistics.StatisticsInteractor
import fr.shiningcat.simplehiit.sharedui.statistics.StatisticsInteractorImpl
import fr.shiningcat.simplehiit.sharedui.statistics.StatisticsViewModel
import fr.shiningcat.simplehiit.sharedui.statistics.StatisticsViewStateMapper
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val statisticsModule =
    module {
        // ViewStateMapper - factory since it was ViewModelComponent scoped
        factory {
            StatisticsViewStateMapper(
                formatLongDurationMsAsSmallestHhMmSsStringUseCase = get(),
                logger = get(),
            )
        }

        // Interactor - factory since it was ViewModelComponent scoped
        factory<StatisticsInteractor> {
            StatisticsInteractorImpl(
                getAllUsersUseCase = get(),
                getStatsForUserUseCase = get(),
                deleteSessionsForUserUseCase = get(),
                resetWholeAppUseCase = get(),
            )
        }

        // ViewModel
        viewModel {
            StatisticsViewModel(
                statisticsInteractor = get(),
                mapper = get(),
                timeProvider = get(),
                mainDispatcher = get(named("MainDispatcher")),
                logger = get(),
            )
        }
    }
