package fr.shining_cat.simplehiit.android.tv.ui.home.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import fr.shining_cat.simplehiit.android.tv.ui.home.HomeInteractor
import fr.shining_cat.simplehiit.android.tv.ui.home.HomeInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
interface HomeModule {

    @Binds
    fun bindHomeInteractor(
        homeInteractor: HomeInteractorImpl
    ): HomeInteractor

}