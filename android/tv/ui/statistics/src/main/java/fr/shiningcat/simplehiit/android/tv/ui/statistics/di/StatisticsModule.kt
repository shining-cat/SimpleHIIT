package fr.shiningcat.simplehiit.android.tv.ui.statistics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import fr.shiningcat.simplehiit.android.tv.ui.statistics.StatisticsInteractor
import fr.shiningcat.simplehiit.android.tv.ui.statistics.StatisticsInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
interface StatisticsModule {
    @Binds
    fun bindStatisticsInteractor(statisticsInteractor: StatisticsInteractorImpl): StatisticsInteractor
}
