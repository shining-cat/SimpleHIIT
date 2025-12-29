package fr.shiningcat.simplehiit.sharedui.session.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import fr.shiningcat.simplehiit.sharedui.session.SessionInteractor
import fr.shiningcat.simplehiit.sharedui.session.SessionInteractorImpl
import fr.shiningcat.simplehiit.sharedui.session.SoundPoolFactory
import fr.shiningcat.simplehiit.sharedui.session.SoundPoolFactoryImpl
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object SessionModuleHilt {
    @Provides
    fun provideSessionInteractor(sessionInteractor: SessionInteractorImpl): SessionInteractor = sessionInteractor
}

@Module
@InstallIn(SingletonComponent::class)
object SessionFactoryModuleHilt {
    @Provides
    @Singleton
    fun provideSoundPoolFactory(): SoundPoolFactory = SoundPoolFactoryImpl()
}
