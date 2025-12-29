@file:Suppress("ktlint:standard:filename") // Temporary during Hilt->Koin migration

package fr.shiningcat.simplehiit.commonutils.di

import android.content.Context
import android.content.pm.ApplicationInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.HiitLoggerImpl
import fr.shiningcat.simplehiit.commonutils.TimeProvider
import fr.shiningcat.simplehiit.commonutils.TimeProviderImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

/**
 * TEMPORARY HILT MODULE - FOR GRADUAL MIGRATION TO KOIN
 *
 * This module exists alongside the Koin utilsModule to support existing consumers
 * that still use Hilt. Once all consuming modules are migrated to Koin, this file
 * and the original UtilsModule.kt will be removed.
 *
 * DO NOT ADD NEW DEPENDENCIES HERE - use the Koin utilsModule instead.
 */
@Module
@InstallIn(SingletonComponent::class)
object UtilsModuleHilt {
    @Provides
    fun provideHiitLogger(
        @ApplicationContext context: Context,
    ): HiitLogger {
        val isDebug: Boolean =
            (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        return HiitLoggerImpl(isDebug)
    }

    @Provides
    fun provideTimeProvider(): TimeProvider = TimeProviderImpl()

    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher
