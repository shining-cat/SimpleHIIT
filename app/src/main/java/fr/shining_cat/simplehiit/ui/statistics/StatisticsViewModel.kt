package fr.shining_cat.simplehiit.ui.statistics

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.domain.usecases.DeleteSessionsForUserUseCase
import fr.shining_cat.simplehiit.domain.usecases.GetAllUsersUseCase
import fr.shining_cat.simplehiit.domain.usecases.GetStatsForUserUseCase
import fr.shining_cat.simplehiit.domain.usecases.ResetWholeAppUseCase
import fr.shining_cat.simplehiit.ui.AbstractLoggerViewModel
import fr.shining_cat.simplehiit.ui.home.HomeDialog
import fr.shining_cat.simplehiit.ui.settings.SettingsDialog
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getStatsForUserUseCase: GetStatsForUserUseCase,
    private val deleteSessionsForUserUseCase: DeleteSessionsForUserUseCase,
    private val resetWholeAppUseCase: ResetWholeAppUseCase,
    private val mapper: StatisticsMapper,
    private val hiitLogger: HiitLogger
) : AbstractLoggerViewModel(hiitLogger) {

    private val _screenViewState =
        MutableStateFlow<StatisticsViewState>(StatisticsViewState.StatisticsLoading)
    val screenViewState = _screenViewState.asStateFlow()
    private val _allUsersViewState = MutableStateFlow<List<User>>(emptyList())
    val allUsersViewState = _allUsersViewState.asStateFlow()
    private val _dialogViewState = MutableStateFlow<StatisticsDialog>(StatisticsDialog.None)
    val dialogViewState = _dialogViewState.asStateFlow()


    init {
        viewModelScope.launch {
            getAllUsersUseCase.execute().collect() {
                when (it) {
                    is Output.Success -> {
                        if (it.result.isEmpty()) {//users list retrieved is empty -> no users yet. Special case
                            _screenViewState.emit(StatisticsViewState.StatisticsNoUsers)
                        } else {//nominal case
                            _allUsersViewState.emit(it.result)
                            retrieveStatsForUser(it.result[0])
                        }
                    }
                    is Output.Error -> { //failed retrieving users list -> fatal error
                        _screenViewState.emit(StatisticsViewState.StatisticsFatalError(it.errorCode.code))
                    }
                }
            }
        }
    }

    fun retrieveStatsForUser(user: User) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val statisticsOutput = getStatsForUserUseCase.execute(user = user, now = now)
            when (statisticsOutput) {
                is Output.Success -> {
                    _screenViewState.emit(mapper.map(statisticsOutput.result))
                }
                is Output.Error -> { //failed retrieving statistics for selected user -> special error for this user
                    _screenViewState.emit(StatisticsViewState.StatisticsError(statisticsOutput.errorCode.code, user))
                }
            }
        }
    }

    fun openPickUser() {
        viewModelScope.launch {
            val currentViewState = screenViewState.value
            if (currentViewState is StatisticsViewState.StatisticsNominal) {
                val users = allUsersViewState.value
                _dialogViewState.emit(StatisticsDialog.SelectUserDialog(users))
            } else {
                hiitLogger.e(
                    "StatisticsViewModel",
                    "openPickUser:: current state does not allow this now"
                )
            }
        }
    }

    fun deleteAllSessionsForUser(user: User) {
        viewModelScope.launch {
            _dialogViewState.emit(StatisticsDialog.SettingsDialogConfirmDeleteAllSessionsForUser(user))
        }
    }

    fun deleteAllSessionsForUserConfirmation(user: User) {
        viewModelScope.launch {
            deleteSessionsForUserUseCase.execute(user.id)
            _dialogViewState.emit(StatisticsDialog.None)
        }
    }

    fun resetWholeApp() {
        viewModelScope.launch {
            _dialogViewState.emit(StatisticsDialog.StatisticsDialogConfirmWholeReset)
        }
    }

    fun resetWholeAppConfirmationDeleteEverything() {
        viewModelScope.launch {
            resetWholeAppUseCase.execute()
        }
    }

    fun cancelDialog() {
        viewModelScope.launch {
            _dialogViewState.emit(StatisticsDialog.None)
        }
    }
}