package fr.shiningcat.simplehiit.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.shiningcat.simplehiit.data.repositories.LanguageRepositoryImpl
import fr.shiningcat.simplehiit.data.repositories.SessionsRepositoryImpl
import fr.shiningcat.simplehiit.data.repositories.SettingsRepositoryImpl
import fr.shiningcat.simplehiit.data.repositories.UsersRepositoryImpl
import fr.shiningcat.simplehiit.domain.common.datainterfaces.LanguageRepository
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SessionsRepository
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import fr.shiningcat.simplehiit.domain.common.datainterfaces.UsersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Singleton
    @Binds
    fun bindsUsersRepository(usersRepository: UsersRepositoryImpl): UsersRepository

    @Singleton
    @Binds
    fun bindsSessionsRepository(sessionsRepository: SessionsRepositoryImpl): SessionsRepository

    @Singleton
    @Binds
    fun bindsSettingsRepository(settingsRepository: SettingsRepositoryImpl): SettingsRepository

    @Singleton
    @Binds
    fun bindsLanguageRepository(languageRepository: LanguageRepositoryImpl): LanguageRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DataDispatcherModule {
    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher
