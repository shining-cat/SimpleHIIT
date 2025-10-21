package fr.shiningcat.simplehiit.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.shiningcat.simplehiit.data.repositories.LanguageRepositoryImpl
import fr.shiningcat.simplehiit.data.repositories.SimpleHiitRepositoryImpl
import fr.shiningcat.simplehiit.domain.common.datainterfaces.LanguageRepository
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Singleton
    @Binds
    fun bindsSimpleHiitRepository(simpleHiitRepository: SimpleHiitRepositoryImpl): SimpleHiitRepository

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
