package fr.shining_cat.simplehiit.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.domain.models.DurationStringFormatter
import fr.shining_cat.simplehiit.domain.models.Exercise
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.domain.usecases.GetGeneralSettingsUseCase
import fr.shining_cat.simplehiit.domain.usecases.GetHomeSettingsUseCase
import fr.shining_cat.simplehiit.ui.AbstractLoggerViewModel
import fr.shining_cat.simplehiit.ui.home.HomeViewState
import fr.shining_cat.simplehiit.ui.statistics.StatisticsDialog
import fr.shining_cat.simplehiit.ui.statistics.StatisticsViewState
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val mapper: SessionMapper,
    private val getGeneralSettingsUseCase: GetGeneralSettingsUseCase,
    private val getHomeSettingsUseCase: GetHomeSettingsUseCase,
    private val hiitLogger: HiitLogger

) : AbstractLoggerViewModel(hiitLogger) {

    private val _screenViewState = MutableStateFlow<SessionViewState>(SessionViewState.SessionLoading)
    val screenViewState = _screenViewState.asStateFlow()
    private val _dialogViewState = MutableStateFlow<SessionDialog>(SessionDialog.None)
    val dialogViewState = _dialogViewState.asStateFlow()


    private var isInitialized = false

    private var durationStringFormatter = DurationStringFormatter()
    private var exercisesList: List<Exercise> = emptyList()

    fun init(durationStringFormatter: DurationStringFormatter) {
        if (!isInitialized) {
            this.durationStringFormatter = durationStringFormatter
            //
            viewModelScope.launch {

            }
            //
            isInitialized = true
        }
    }

    fun pause() {
        viewModelScope.launch {
            //todo: pause session mechanic
            _dialogViewState.emit(SessionDialog.PauseDialog)
        }
    }

    fun resume() {
        viewModelScope.launch {
            //todo: relaunch session mechanic
            _dialogViewState.emit(SessionDialog.None)
        }
    }

}