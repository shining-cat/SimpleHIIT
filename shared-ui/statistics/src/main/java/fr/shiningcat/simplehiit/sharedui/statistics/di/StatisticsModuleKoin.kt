package fr.shiningcat.simplehiit.sharedui.statistics.di

import fr.shiningcat.simplehiit.sharedui.statistics.StatisticsInteractor
import fr.shiningcat.simplehiit.sharedui.statistics.StatisticsInteractorImpl
import fr.shiningcat.simplehiit.sharedui.statistics.StatisticsPresenter
import fr.shiningcat.simplehiit.sharedui.statistics.StatisticsViewStateMapper
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

        // Presenter - Pure Kotlin business logic
        factory {
            StatisticsPresenter(
                statisticsInteractor = get(),
                mapper = get(),
                timeProvider = get(),
                logger = get(),
            )
        }
    }
