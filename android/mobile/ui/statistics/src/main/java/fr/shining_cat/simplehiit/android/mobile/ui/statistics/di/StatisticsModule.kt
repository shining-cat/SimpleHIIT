package fr.shining_cat.simplehiit.android.mobile.ui.statistics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import fr.shining_cat.simplehiit.android.mobile.ui.statistics.StatisticsInteractor
import fr.shining_cat.simplehiit.android.mobile.ui.statistics.StatisticsInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
interface StatisticsModule {

    @Binds
    fun bindStatisticsInteractor(
        statisticsInteractor: StatisticsInteractorImpl
    ): StatisticsInteractor

}