package fr.shiningcat.simplehiit.data.local.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.data.di.IoDispatcher
import fr.shiningcat.simplehiit.data.local.database.SimpleHiitDatabase
import fr.shiningcat.simplehiit.data.local.database.SimpleHiitDatabase.Companion.SimpleHiitDatabaseName
import fr.shiningcat.simplehiit.data.local.database.dao.SessionRecordsDao
import fr.shiningcat.simplehiit.data.local.database.dao.UsersDao
import fr.shiningcat.simplehiit.data.local.datastore.SIMPLE_HIIT_DATASTORE_FILENAME
import fr.shiningcat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager
import fr.shiningcat.simplehiit.data.local.datastore.SimpleHiitDataStoreManagerImpl
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {
    @Provides
    fun provideUserDao(simpleHiitDatabase: SimpleHiitDatabase): UsersDao = simpleHiitDatabase.userDao()

    @Provides
    fun provideSessionsDao(simpleHiitDatabase: SimpleHiitDatabase): SessionRecordsDao = simpleHiitDatabase.sessionsDao()

    @Provides
    @Singleton
    fun provideSimpleHiitDatabase(
        @ApplicationContext appContext: Context,
    ): SimpleHiitDatabase =
        Room
            .databaseBuilder(
                context = appContext,
                klass = SimpleHiitDatabase::class.java,
                name = SimpleHiitDatabaseName,
            ).build()

    @Provides
    @Singleton
    fun provideSimpleHiitDataStoreManager(
        @ApplicationContext appContext: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        hiitLogger: HiitLogger,
    ): SimpleHiitDataStoreManager {
        val datastore =
            PreferenceDataStoreFactory.create(
                produceFile = {
                    appContext.preferencesDataStoreFile(SIMPLE_HIIT_DATASTORE_FILENAME)
                },
            )
        return SimpleHiitDataStoreManagerImpl(datastore, ioDispatcher, hiitLogger)
    }
}
