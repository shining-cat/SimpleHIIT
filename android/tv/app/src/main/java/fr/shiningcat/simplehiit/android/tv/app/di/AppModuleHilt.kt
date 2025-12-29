package fr.shiningcat.simplehiit.android.tv.app.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.shiningcat.simplehiit.android.tv.app.locale.LocaleManagerImpl
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.AndroidVersionProvider
import fr.shiningcat.simplehiit.commonutils.AndroidVersionProviderImpl
import fr.shiningcat.simplehiit.data.local.localemanager.LocaleManager
import fr.shiningcat.simplehiit.domain.common.DurationStringFormatter
import fr.shiningcat.simplehiit.domain.common.di.DigitsFormat
import fr.shiningcat.simplehiit.domain.common.di.ShortFormat

/**
 * Temporary Hilt module during migration to Koin.
 * This module exists to maintain Hilt compatibility while the migration is in progress.
 *
 * DELETE THIS FILE after full migration to Koin is complete.
 */
@Module
@InstallIn(SingletonComponent::class)
interface AppModuleHilt {
    @Binds
    fun bindsLocaleManager(localeManagerImpl: LocaleManagerImpl): LocaleManager

    @Binds
    fun bindsAndroidVersionProvider(androidVersionProviderImpl: AndroidVersionProviderImpl): AndroidVersionProvider
}

@Module
@InstallIn(SingletonComponent::class)
object AppModuleProvidesHilt {
    @DigitsFormat
    @Provides
    fun provideDurationStringFormatterDigits(
        @ApplicationContext context: Context,
    ): DurationStringFormatter =
        DurationStringFormatter(
            hoursMinutesSeconds = context.getString(R.string.hours_minutes_seconds_digits),
            hoursMinutesNoSeconds = context.getString(R.string.hours_minutes_no_seconds_digits),
            hoursNoMinutesNoSeconds = context.getString(R.string.hours_no_minutes_no_seconds_digits),
            minutesSeconds = context.getString(R.string.minutes_seconds_digits),
            minutesNoSeconds = context.getString(R.string.minutes_no_seconds_digits),
            seconds = context.getString(R.string.seconds_digits),
        )

    @ShortFormat
    @Provides
    fun provideDurationStringFormatterShort(
        @ApplicationContext context: Context,
    ): DurationStringFormatter =
        DurationStringFormatter(
            hoursMinutesSeconds = context.getString(R.string.hours_minutes_seconds_short),
            hoursMinutesNoSeconds = context.getString(R.string.hours_minutes_no_seconds_short),
            hoursNoMinutesNoSeconds = context.getString(R.string.hours_no_minutes_no_seconds_short),
            minutesSeconds = context.getString(R.string.minutes_seconds_short),
            minutesNoSeconds = context.getString(R.string.minutes_no_seconds_short),
            seconds = context.getString(R.string.seconds_short),
        )
}
