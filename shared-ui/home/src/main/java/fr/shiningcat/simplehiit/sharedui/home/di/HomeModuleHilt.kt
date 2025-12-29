@file:Suppress("ktlint:standard:filename") // Temporary during Hilt->Koin migration

package fr.shiningcat.simplehiit.sharedui.home.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import fr.shiningcat.simplehiit.sharedui.home.HomeInteractor
import fr.shiningcat.simplehiit.sharedui.home.HomeInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
interface HomeModule {
    @Binds
    fun bindHomeInteractor(homeInteractor: HomeInteractorImpl): HomeInteractor
}
