package fr.shiningcat.simplehiit.commonutils.di

import android.content.pm.ApplicationInfo
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.HiitLoggerImpl
import fr.shiningcat.simplehiit.commonutils.TimeProvider
import fr.shiningcat.simplehiit.commonutils.TimeProviderImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val utilsModule =
    module {
        factory<HiitLogger> {
            val isDebug = (androidContext().applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
            HiitLoggerImpl(isDebugBuild = isDebug)
        }

        single<TimeProvider> {
            TimeProviderImpl()
        }
    }

val dispatchersModule =
    module {
        single<CoroutineDispatcher>(named("DefaultDispatcher")) { Dispatchers.Default }
        single<CoroutineDispatcher>(named("MainDispatcher")) { Dispatchers.Main }
        single<CoroutineDispatcher>(named("IoDispatcher")) { Dispatchers.IO }
        single<CoroutineDispatcher>(named("TimerDispatcher")) { Dispatchers.Default }
    }
