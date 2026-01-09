package fr.shiningcat.simplehiit.android.shared.session

import android.media.SoundPool
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.sharedui.session.SessionDialog
import fr.shiningcat.simplehiit.sharedui.session.SessionPresenter
import fr.shiningcat.simplehiit.sharedui.session.SessionViewState
import fr.shiningcat.simplehiit.sharedui.session.SoundPoolFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Android ViewModel wrapper for session screen.
 * Manages SoundPool (Android-specific audio) and lifecycle.
 * Delegates all business logic to SessionPresenter.
 */
class SessionViewModel(
    private val presenter: SessionPresenter,
    private val soundPoolFactory: SoundPoolFactory,
    private val mainDispatcher: CoroutineDispatcher,
    private val logger: HiitLogger,
) : ViewModel() {
    // SoundPool management (Android-specific, stays in ViewModel)
    private val soundPool: SoundPool = soundPoolFactory.create()
    private var beepSoundLoadedId: Int? = null

    // State exposure (from Presenter)
    val screenViewState: StateFlow<SessionViewState> = presenter.screenViewState
    val dialogViewState: StateFlow<SessionDialog> = presenter.dialogViewState

    init {
        logger.d("SessionViewModel", "initializing thin wrapper")
        setupSoundPool()
        setupBeepSignalCollector()
    }

    private fun setupSoundPool() {
        soundPool.setOnLoadCompleteListener { _: SoundPool, sampleId: Int, status: Int ->
            if (status == 0) {
                beepSoundLoadedId = sampleId
                logger.d("SessionViewModel", "sound loaded, notifying presenter")
                viewModelScope.launch(mainDispatcher) {
                    presenter.onSoundLoaded()
                }
            } else {
                logger.e("SessionViewModel", "sound failed to load")
            }
        }
    }

    private fun setupBeepSignalCollector() {
        viewModelScope.launch(mainDispatcher) {
            presenter.beepSignal.collect {
                playBeepSound()
            }
        }
    }

    private fun playBeepSound() {
        beepSoundLoadedId?.let { soundId ->
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
        } ?: logger.e("SessionViewModel", "attempted to play beep but sound not loaded")
    }

    fun getSoundPool(): SoundPool = soundPool

    fun isSoundLoaded() = beepSoundLoadedId != null

    fun pause() {
        viewModelScope.launch(mainDispatcher) {
            presenter.pause()
        }
    }

    fun resume() {
        viewModelScope.launch(mainDispatcher) {
            presenter.resume()
        }
    }

    fun reinitializeSession() {
        viewModelScope.launch(mainDispatcher) {
            presenter.resetAndStart()
        }
    }

    fun abortSession() {
        viewModelScope.launch(mainDispatcher) {
            presenter.abortSession()
        }
    }

    override fun onCleared() {
        super.onCleared()
        logger.d("SessionViewModel", "onCleared - releasing SoundPool and cleaning up presenter")
        soundPool.release()
        presenter.cleanup()
    }
}
