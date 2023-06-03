package fr.shining_cat.simplehiit.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.commondomain.Output
import fr.shining_cat.simplehiit.commondomain.models.DurationStringFormatter
import fr.shining_cat.simplehiit.commondomain.models.User
import fr.shining_cat.simplehiit.commondomain.usecases.DeleteSessionsForUserUseCase
import fr.shining_cat.simplehiit.commondomain.usecases.GetAllUsersUseCase
import fr.shining_cat.simplehiit.commondomain.usecases.GetStatsForUserUseCase
import fr.shining_cat.simplehiit.commondomain.usecases.ResetWholeAppUseCase
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.commonutils.TimeProvider
import fr.shining_cat.simplehiit.di.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
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
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val timeProvider: TimeProvider,
    private val hiitLogger: HiitLogger
) : ViewModel() {

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
            viewModelScope.launch(context = mainDispatcher) {
                getAllUsersUseCase.execute().collect() {
                    when (it) {
                        is Output.Success -> {
                            //nominal case
                            _allUsersViewState.emit(it.result)
                            retrieveStatsForUser(it.result[0])
                        }

                        is Output.Error -> {
                            if (it.errorCode == fr.shining_cat.simplehiit.commondomain.Constants.Errors.NO_USERS_FOUND) {
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
        viewModelScope.launch(context = mainDispatcher) {
            val now = timeProvider.getCurrentTimeMillis()
            when (val statisticsOutput = getStatsForUserUseCase.execute(user = user, now = now)) {
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
        viewModelScope.launch(context = mainDispatcher) {
            val users = allUsersViewState.value
            _dialogViewState.emit(StatisticsDialog.SelectUser(users))
        }
    }

    fun deleteAllSessionsForUser(user: User) {
        hiitLogger.d(
            "StatisticsViewModel",
            "deleteAllSessionsForUser::user = $user"
        )
        viewModelScope.launch(context = mainDispatcher) {
            _dialogViewState.emit(
                StatisticsDialog.ConfirmDeleteAllSessionsForUser(user)
            )
        }
    }

    fun deleteAllSessionsForUserConfirmation(user: User) {
        viewModelScope.launch(context = mainDispatcher) {
            deleteSessionsForUserUseCase.execute(user.id)
            _dialogViewState.emit(StatisticsDialog.None)
            //refetch to force refresh:
            retrieveStatsForUser(user = user)
        }
    }

    fun resetWholeApp() {
        viewModelScope.launch(context = mainDispatcher) {
            _dialogViewState.emit(StatisticsDialog.ConfirmWholeReset)
        }
    }

    fun resetWholeAppConfirmationDeleteEverything() {
        viewModelScope.launch(context = mainDispatcher) {
            resetWholeAppUseCase.execute()
        }
    }

    fun cancelDialog() {
        viewModelScope.launch(context = mainDispatcher) {
            _dialogViewState.emit(StatisticsDialog.None)
        }
    }
}