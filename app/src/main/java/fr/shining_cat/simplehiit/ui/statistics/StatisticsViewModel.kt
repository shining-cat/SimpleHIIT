package fr.shining_cat.simplehiit.ui.statistics

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.DurationStringFormatter
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.domain.usecases.DeleteSessionsForUserUseCase
import fr.shining_cat.simplehiit.domain.usecases.GetAllUsersUseCase
import fr.shining_cat.simplehiit.domain.usecases.GetStatsForUserUseCase
import fr.shining_cat.simplehiit.domain.usecases.ResetWholeAppUseCase
import fr.shining_cat.simplehiit.ui.AbstractLoggerViewModel
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
        MutableStateFlow<StatisticsViewState>(StatisticsViewState.Loading)
    val screenViewState = _screenViewState.asStateFlow()
    private val _allUsersViewState = MutableStateFlow<List<User>>(emptyList())
    val allUsersViewState = _allUsersViewState.asStateFlow()
    private val _dialogViewState = MutableStateFlow<StatisticsDialog>(StatisticsDialog.None)
    val dialogViewState = _dialogViewState.asStateFlow()

    private var isInitialized = false

    private var durationStringFormatter = DurationStringFormatter()

    fun init(durationStringFormatter: DurationStringFormatter) {
        if (!isInitialized) {
            this.durationStringFormatter = durationStringFormatter
            //
            viewModelScope.launch {
                getAllUsersUseCase.execute().collect() {
                    when (it) {
                        is Output.Success -> {
                            //nominal case
                            _allUsersViewState.emit(it.result)
                            retrieveStatsForUser(it.result[0])
                        }
                        is Output.Error -> {
                            if (it.errorCode == Constants.Errors.NO_USERS_FOUND) {
                                //users list retrieved is empty -> no users yet. Special case
                                _screenViewState.emit(StatisticsViewState.NoUsers)
                            } else {
                                //failed retrieving users list -> fatal error
                                _screenViewState.emit(StatisticsViewState.FatalError(it.errorCode.code))
                            }
                        }
                    }
                }
            }
            isInitialized = true
        }
    }

    fun retrieveStatsForUser(user: User) {
        hiitLogger.d("StatisticsViewModel", "retrieveStatsForUser::user = $user")
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val statisticsOutput = getStatsForUserUseCase.execute(user = user, now = now)
            when (statisticsOutput) {
                is Output.Success -> {
                    _screenViewState.emit(
                        mapper.map(
                            statisticsOutput.result,
                            durationStringFormatter
                        )
                    )
                }
                is Output.Error -> { //failed retrieving statistics for selected user -> special error for this user
                    _screenViewState.emit(
                        StatisticsViewState.Error(
                            statisticsOutput.errorCode.code,
                            user
                        )
                    )
                }
            }
        }
    }

    fun openPickUser() {
        viewModelScope.launch {
            val users = allUsersViewState.value
            _dialogViewState.emit(StatisticsDialog.SelectUser(users))
        }
    }

    fun deleteAllSessionsForUser(user: User) {
        hiitLogger.d(
            "StatisticsViewModel",
            "deleteAllSessionsForUser::user = $user"
        )
        viewModelScope.launch {
            _dialogViewState.emit(
                StatisticsDialog.ConfirmDeleteAllSessionsForUser(user)
            )
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
            _dialogViewState.emit(StatisticsDialog.ConfirmWholeReset)
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