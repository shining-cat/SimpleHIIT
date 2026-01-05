package fr.shiningcat.simplehiit.android.shared.statistics

import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val androidSharedStatisticsModule =
    module {

        viewModel {
            StatisticsViewModel(
                statisticsPresenter = get(),
                mainDispatcher = get(named("MainDispatcher")),
            )
        }
    }
