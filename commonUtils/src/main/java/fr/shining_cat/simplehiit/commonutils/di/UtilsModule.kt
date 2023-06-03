package fr.shining_cat.simplehiit.commonutils.di

import android.content.Context
import android.content.pm.ApplicationInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.commonutils.HiitLoggerImpl
import fr.shining_cat.simplehiit.commonutils.TimeProvider
import fr.shining_cat.simplehiit.commonutils.TimeProviderImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    fun provideHiitLogger(@ApplicationContext context: Context): HiitLogger {
        val isDebug: Boolean =
            (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        return HiitLoggerImpl(isDebug)
    }

    @Provides
    fun provideTimeProvider(): TimeProvider {
        return TimeProviderImpl()
    }

    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher
