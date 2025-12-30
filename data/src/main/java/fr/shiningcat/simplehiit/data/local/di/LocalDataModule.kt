package fr.shiningcat.simplehiit.data.local.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import fr.shiningcat.simplehiit.data.local.database.SimpleHiitDatabase
import fr.shiningcat.simplehiit.data.local.database.SimpleHiitDatabase.Companion.SimpleHiitDatabaseName
import fr.shiningcat.simplehiit.data.local.datastore.SIMPLE_HIIT_DATASTORE_FILENAME
import fr.shiningcat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager
import fr.shiningcat.simplehiit.data.local.datastore.SimpleHiitDataStoreManagerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val localDataModule =
    module {
        single {
            Room
                .databaseBuilder(
                    context = androidContext(),
                    klass = SimpleHiitDatabase::class.java,
                    name = SimpleHiitDatabaseName,
                ).build()
        }

        single { get<SimpleHiitDatabase>().userDao() }

        single { get<SimpleHiitDatabase>().sessionsDao() }

        single {
            PreferenceDataStoreFactory.create(
                produceFile = {
                    androidContext().preferencesDataStoreFile(SIMPLE_HIIT_DATASTORE_FILENAME)
                },
            )
        }

        single<SimpleHiitDataStoreManager> {
            SimpleHiitDataStoreManagerImpl(
                dataStore = get(),
                ioDispatcher = get(named("IoDispatcher")),
                hiitLogger = get(),
            )
        }
    }
