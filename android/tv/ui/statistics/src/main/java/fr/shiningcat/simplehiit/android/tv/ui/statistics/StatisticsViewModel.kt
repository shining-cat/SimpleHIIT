package fr.shiningcat.simplehiit.android.tv.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.TimeProvider
import fr.shiningcat.simplehiit.commonutils.di.MainDispatcher
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statisticsInteractor: StatisticsInteractor,
    private val mapper: StatisticsViewStateMapper,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val timeProvider: TimeProvider,
    private val hiitLogger: HiitLogger
) : ViewModel() {

    private val _screenViewState =
        MutableStateFlow<StatisticsViewState>(StatisticsViewState.Loading)
    val screenViewState = _screenViewState.asStateFlow()
    private val _dialogViewState = MutableStateFlow<StatisticsDialog>(StatisticsDialog.None)
    val dialogViewState = _dialogViewState.asStateFlow()

    private var isInitialized = false
    private var allUsers =
        emptyList<User>() // the list of users can't be modified from this screen so it's safe to tie this to the whole lifetime of this ViewModel

    private var durationStringFormatter = DurationStringFormatter()

    fun init(durationStringFormatter: DurationStringFormatter) {
        if (!isInitialized) {
            this.durationStringFormatter = durationStringFormatter
            //
            viewModelScope.launch(context = mainDispatcher) {
                statisticsInteractor.getAllUsers().collect {
                    when (it) {
                        is Output.Success -> {
                            //nominal case
                            allUsers = it.result
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
        viewModelScope.launch(context = mainDispatcher) {
            val now = timeProvider.getCurrentTimeMillis()
            val statisticsOutput =
                statisticsInteractor.getStatsForUser(user = user, now = now)
            val moreThanOneUser = allUsers.size > 1
            when (statisticsOutput) {
                is Output.Success -> {
                    _screenViewState.emit(
                        mapper.map(
                            showUsersSwitch = moreThanOneUser,
                            userStats = statisticsOutput.result,
                            durationStringFormatter = durationStringFormatter
                        )
                    )
                }

                is Output.Error -> { //failed retrieving statistics for selected user -> special error for this user
                    _screenViewState.emit(
                        StatisticsViewState.Error(
                            errorCode = statisticsOutput.errorCode.code,
                            user = user,
                            showUsersSwitch = moreThanOneUser,
                        )
                    )
                }
            }
        }
    }

    fun openPickUser() {
        viewModelScope.launch(context = mainDispatcher) {
            if (allUsers.size < 2) hiitLogger.e(
                "StatisticsViewModel",
                "openPickUser called but there is only 1 user or less"
            )
            _dialogViewState.emit(StatisticsDialog.SelectUser(allUsers))
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
            statisticsInteractor.deleteSessionsForUser(user.id)
            _dialogViewState.emit(StatisticsDialog.None)
            //force refresh:
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
            statisticsInteractor.resetWholeApp()
        }
    }

    fun cancelDialog() {
        viewModelScope.launch(context = mainDispatcher) {
            _dialogViewState.emit(StatisticsDialog.None)
        }
    }
}