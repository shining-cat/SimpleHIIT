package fr.shining_cat.simplehiit.data.local.di

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
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
import fr.shining_cat.simplehiit.data.local.datastore.SIMPLE_HIIT_DATASTORE_FILENAME
import fr.shining_cat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager
import fr.shining_cat.simplehiit.data.local.datastore.SimpleHiitDataStoreManagerImpl
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
    @Singleton
    fun provideSimpleHiitDataStoreManager(
        @ApplicationContext appContext: Context,
        hiitLogger: HiitLogger
    ): SimpleHiitDataStoreManager {
        val datastore = PreferenceDataStoreFactory.create(
            produceFile = {
                appContext.preferencesDataStoreFile(SIMPLE_HIIT_DATASTORE_FILENAME)
            }
        )
        return SimpleHiitDataStoreManagerImpl(datastore, hiitLogger)
    }

}