package fr.shiningcat.simplehiit.android.mobile.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.TimeProvider
import fr.shiningcat.simplehiit.commonutils.di.MainDispatcher
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel
    @Inject
    constructor(
        private val statisticsInteractor: StatisticsInteractor,
        private val mapper: StatisticsViewStateMapper,
        @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
        private val timeProvider: TimeProvider,
        private val hiitLogger: HiitLogger,
    ) : ViewModel() {
        // Statistics are loaded on-demand per user, not a continuous reactive stream
        private val _screenViewState =
            MutableStateFlow<StatisticsViewState>(StatisticsViewState.Loading)
        val screenViewState = _screenViewState.asStateFlow()

        private val _dialogViewState = MutableStateFlow<StatisticsDialog>(StatisticsDialog.None)
        val dialogViewState = _dialogViewState.asStateFlow()

        private val usersFlow =
            statisticsInteractor
                .getAllUsers()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                    initialValue = Output.Success(emptyList()),
                )

        init {
            hiitLogger.d("StatisticsViewModel", "initialized with hybrid state management")
            // Observe users and load stats for first user when available
            viewModelScope.launch(context = mainDispatcher) {
                usersFlow.collect { usersOutput ->
                    when (usersOutput) {
                        is Output.Success -> retrieveStatsForUser(usersOutput.result[0])
                        is Output.Error -> _screenViewState.emit(mapper.mapUsersError(usersOutput.errorCode))
                    }
                }
            }
        }

        fun retrieveStatsForUser(user: User) {
            hiitLogger.d("StatisticsViewModel", "retrieveStatsForUser::user = $user")
            viewModelScope.launch(context = mainDispatcher) {
                val usersOutput = usersFlow.value
                when (usersOutput) {
                    is Output.Success -> {
                        val now = timeProvider.getCurrentTimeMillis()
                        val statisticsOutput =
                            statisticsInteractor.getStatsForUser(user = user, now = now)
                        _screenViewState.emit(
                            when (statisticsOutput) {
                                is Output.Success ->
                                    mapper.map(
                                        allUsers = usersOutput.result,
                                        selectedUserStatistics = statisticsOutput.result,
                                    )

                                is Output.Error ->
                                    StatisticsViewState.Error(
                                        allUsers = usersOutput.result,
                                        selectedUser = user,
                                        errorCode = statisticsOutput.errorCode.code,
                                    )
                            },
                        )
                    }

                    is Output.Error -> {
                        hiitLogger.e(
                            "StatisticsViewModel",
                            "retrieveStatsForUser called but users flow is in error state",
                        )
                        _screenViewState.emit(mapper.mapUsersError(usersOutput.errorCode))
                    }
                }
            }
        }

        fun deleteAllSessionsForUser(user: User) {
            hiitLogger.d(
                "StatisticsViewModel",
                "deleteAllSessionsForUser::user = $user",
            )
            viewModelScope.launch(context = mainDispatcher) {
                _dialogViewState.emit(
                    StatisticsDialog.ConfirmDeleteAllSessionsForUser(user),
                )
            }
        }

        fun deleteAllSessionsForUserConfirmation(user: User) {
            viewModelScope.launch(context = mainDispatcher) {
                statisticsInteractor.deleteSessionsForUser(user.id)
                _dialogViewState.emit(StatisticsDialog.None)
                // force refresh:
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
