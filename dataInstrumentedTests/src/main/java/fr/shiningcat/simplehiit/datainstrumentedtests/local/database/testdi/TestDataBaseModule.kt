package fr.shiningcat.simplehiit.datainstrumentedtests.local.database.testdi

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.shiningcat.simplehiit.data.local.database.SimpleHiitDatabase
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestDataBaseModule {
    @Provides
    @Named("test_db")
    fun provideInMemoryDb(
        @ApplicationContext context: Context,
    ) = Room
        .inMemoryDatabaseBuilder(
            context,
            SimpleHiitDatabase::class.java,
        ).allowMainThreadQueries()
        .build()
}
