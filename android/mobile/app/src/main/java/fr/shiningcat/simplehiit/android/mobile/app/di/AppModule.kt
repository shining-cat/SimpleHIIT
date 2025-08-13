package fr.shiningcat.simplehiit.android.mobile.app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.DurationStringFormatter
import fr.shiningcat.simplehiit.domain.common.di.DigitsFormat
import fr.shiningcat.simplehiit.domain.common.di.ShortFormat

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
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
