package fr.shining_cat.simplehiit.data.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.shining_cat.simplehiit.data.local.database.SimpleHiitDatabase
import fr.shining_cat.simplehiit.data.local.database.SimpleHiitDatabase.Companion.SimpleHiitDatabaseName
import fr.shining_cat.simplehiit.data.local.database.dao.SessionsDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideUserDao(simpleHiitDatabase: SimpleHiitDatabase): UsersDao {
        return simpleHiitDatabase.userDao()
    }

    @Provides
    fun provideSessionsDao(simpleHiitDatabase: SimpleHiitDatabase): SessionsDao {
        return simpleHiitDatabase.sessionsDao()
    }

    @Provides
    @Singleton
    fun provideSimpleHiitDatabase(@ApplicationContext appContext: Context): SimpleHiitDatabase {
        return Room.databaseBuilder(
            appContext,
            SimpleHiitDatabase::class.java,
            SimpleHiitDatabaseName
        ).build()
    }
}