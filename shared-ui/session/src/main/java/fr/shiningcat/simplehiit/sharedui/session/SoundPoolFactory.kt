package fr.shiningcat.simplehiit.sharedui.session

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool

interface SoundPoolFactory {
    fun create(): SoundPool
}

class SoundPoolFactoryImpl : SoundPoolFactory {
    override fun create(): SoundPool =
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
