package fr.shiningcat.simplehiit.android.mobile.ui.home.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import fr.shiningcat.simplehiit.android.mobile.ui.home.HomeInteractor
import fr.shiningcat.simplehiit.android.mobile.ui.home.HomeInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
interface HomeModule {

    @Binds
    fun bindHomeInteractor(
        homeInteractor: HomeInteractorImpl
    ): HomeInteractor
}
