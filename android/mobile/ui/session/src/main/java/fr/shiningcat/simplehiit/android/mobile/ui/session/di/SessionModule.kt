package fr.shiningcat.simplehiit.android.mobile.ui.session.di

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import fr.shiningcat.simplehiit.android.mobile.ui.session.SessionInteractor
import fr.shiningcat.simplehiit.android.mobile.ui.session.SessionInteractorImpl
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.models.DurationStringFormatter

@Module
@InstallIn(ViewModelComponent::class)
object SessionModule {
    @Provides
    fun provideSessionInteractor(sessionInteractor: SessionInteractorImpl): SessionInteractor = sessionInteractor

    @Provides
    fun provideDurationStringFormatter(
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

    @Provides
    @ViewModelScoped
    fun provideSoundPool(): SoundPool =
        SoundPool
            .Builder()
            .setMaxStreams(1)
            .setAudioAttributes(
                AudioAttributes
                    .Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build(),
            ).build()
}
