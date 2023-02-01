package fr.shining_cat.simplehiit.data.local.di

import android.app.Application
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
import fr.shining_cat.simplehiit.data.local.preferences.SIMPLE_HIIT_PREFERENCE_FILENAME
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferencesImpl
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {

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
            context = appContext,
            klass = SimpleHiitDatabase::class.java,
            name = SimpleHiitDatabaseName
        ).build()
    }

    @Provides
    fun provideSimpleHiitPreferences(
        application: Application,
        hiitLogger: HiitLogger
    ): SimpleHiitPreferences {
        return SimpleHiitPreferencesImpl(
            sharedPreferences = application.getSharedPreferences(
                SIMPLE_HIIT_PREFERENCE_FILENAME,
                Context.MODE_PRIVATE
            ),
            hiitLogger = hiitLogger
        )
    }

}