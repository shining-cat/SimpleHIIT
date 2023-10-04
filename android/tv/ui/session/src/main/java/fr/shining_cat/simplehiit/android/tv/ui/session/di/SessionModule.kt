package fr.shining_cat.simplehiit.android.tv.ui.session.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import fr.shining_cat.simplehiit.android.tv.ui.session.SessionInteractor
import fr.shining_cat.simplehiit.android.tv.ui.session.SessionInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
interface SessionModule {

    @Binds
    fun bindSessionInteractor(
        sessionInteractor: SessionInteractorImpl
    ): SessionInteractor

}