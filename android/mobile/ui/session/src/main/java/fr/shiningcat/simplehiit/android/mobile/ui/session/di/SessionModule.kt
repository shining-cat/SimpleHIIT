package fr.shiningcat.simplehiit.android.mobile.ui.session.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import fr.shiningcat.simplehiit.android.mobile.ui.session.SessionInteractor
import fr.shiningcat.simplehiit.android.mobile.ui.session.SessionInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
interface SessionModule {
    @Binds
    fun bindSessionInteractor(sessionInteractor: SessionInteractorImpl): SessionInteractor
}
