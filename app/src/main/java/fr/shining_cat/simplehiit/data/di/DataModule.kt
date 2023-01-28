package fr.shining_cat.simplehiit.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.shining_cat.simplehiit.data.SimpleHiitRepositoryImpl
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsSimpleHiitRepository(
        simpleHiitRepository: SimpleHiitRepositoryImpl
    ):SimpleHiitRepository

    /*@Provides
    fun provideSessionMapper():SessionMapper{
        return SessionMapper()
    }

    @Provides
    fun provideUserMapper():UserMapper{
        return UserMapper()
    }*/

}
