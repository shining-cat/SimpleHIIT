@file:Suppress("ktlint:standard:filename") // Temporary during Hilt->Koin migration

package fr.shiningcat.simplehiit.sharedui.statistics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import fr.shiningcat.simplehiit.sharedui.statistics.StatisticsInteractor
import fr.shiningcat.simplehiit.sharedui.statistics.StatisticsInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
interface StatisticsModule {
    @Binds
    fun bindStatisticsInteractor(statisticsInteractor: StatisticsInteractorImpl): StatisticsInteractor
}
