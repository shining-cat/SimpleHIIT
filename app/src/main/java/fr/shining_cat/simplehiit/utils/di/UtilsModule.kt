package fr.shining_cat.simplehiit.utils.di

import android.content.Context
import android.content.pm.ApplicationInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.shining_cat.simplehiit.utils.HiitLogger
import fr.shining_cat.simplehiit.utils.HiitLoggerImpl

@Module
@InstallIn(ActivityComponent::class)
object UtilsModule {

    @Provides
    fun provideHiitLogger(@ApplicationContext context: Context): HiitLogger {
        val isDebug: Boolean =
            (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        return HiitLoggerImpl(isDebug)
    }

}