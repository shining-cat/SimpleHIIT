package fr.shining_cat.simplehiit.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.shining_cat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.data.repositories.SimpleHiitRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsSimpleHiitRepository(
        simpleHiitRepository: SimpleHiitRepositoryImpl
    ): SimpleHiitRepository

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