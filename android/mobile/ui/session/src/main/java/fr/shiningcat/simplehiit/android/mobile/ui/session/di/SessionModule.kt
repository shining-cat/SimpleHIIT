package fr.shiningcat.simplehiit.android.mobile.ui.session.di

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import fr.shiningcat.simplehiit.android.mobile.ui.session.SessionInteractor
import fr.shiningcat.simplehiit.android.mobile.ui.session.SessionInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
object SessionModule {
    @Provides
    fun provideSessionInteractor(sessionInteractor: SessionInteractorImpl): SessionInteractor = sessionInteractor

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
