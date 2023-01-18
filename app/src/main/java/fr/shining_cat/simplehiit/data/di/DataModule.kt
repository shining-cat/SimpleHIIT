package fr.shining_cat.simplehiit.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.utils.HiitLogger
import fr.shining_cat.simplehiit.data.SimpleHiitRepositoryImpl
import fr.shining_cat.simplehiit.data.local.database.dao.SessionsDao
import fr.shining_cat.simplehiit.data.local.database.dao.SessionsUsersLinkDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.mappers.SessionMapper
import fr.shining_cat.simplehiit.data.mappers.UserMapper
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    /*@Singleton
    @Provides
    fun provideSimpleHiitRepository(
        usersDao: UsersDao,
        sessionsDao: SessionsDao,
        sessionsUsersLinkDao: SessionsUsersLinkDao,
        userMapper: UserMapper,
        sessionMapper: SessionMapper,
        hiitLogger: HiitLogger
    ): SimpleHiitRepository {
        return SimpleHiitRepositoryImpl(
            usersDao = usersDao,
            sessionsDao = sessionsDao,
            sessionsUsersLinkDao = sessionsUsersLinkDao,
            userMapper = userMapper,
            sessionMapper = sessionMapper,
            hiitLogger = hiitLogger
        )
    }*/

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
