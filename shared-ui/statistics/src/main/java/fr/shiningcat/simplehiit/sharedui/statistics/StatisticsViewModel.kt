package fr.shiningcat.simplehiit.sharedui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.NonEmptyList
import fr.shiningcat.simplehiit.commonutils.TimeProvider
import fr.shiningcat.simplehiit.commonutils.di.MainDispatcher
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        private val logger: HiitLogger,
    ) : ViewModel() {
        // Statistics are loaded on-demand per user, not a continuous reactive stream
        private val _screenViewState =
            MutableStateFlow<StatisticsViewState>(StatisticsViewState.Loading)
        val screenViewState = _screenViewState.asStateFlow()

        // UI-driven state - managed manually for dialogs and one-time events
        private val _dialogViewState = MutableStateFlow<StatisticsDialog>(StatisticsDialog.None)
        val dialogViewState = _dialogViewState.asStateFlow()

        // Cache the current users list for use in retrieveStatsForUser and openPickUser
        // Type-safe: NonEmptyList guarantees at least one user
        private var currentUsers: NonEmptyList<User>? = null

        init {
            logger.d("StatisticsViewModel", "initialized with hybrid state management")
            // Observe users and load stats for first user when available
            viewModelScope.launch(context = mainDispatcher) {
                statisticsInteractor.getAllUsers().collect { usersOutput ->
                    when (usersOutput) {
                        is Output.Success -> {
                            currentUsers = usersOutput.result
                            retrieveStatsForUser(usersOutput.result.head)
                        }
                        is Output.Error -> {
                            _screenViewState.emit(mapper.mapUsersError(usersOutput.errorCode))
                        }
                    }
                }
            }
        }

        fun retrieveStatsForUser(user: User) {
            logger.d("StatisticsViewModel", "retrieveStatsForUser::user = $user")
            viewModelScope.launch(context = mainDispatcher) {
                val users = currentUsers
                if (users == null) {
                    logger.e("StatisticsViewModel", "retrieveStatsForUser called but currentUsers is null")
                    return@launch
                }

                val moreThanOneUser = users.size > 1
                val now = timeProvider.getCurrentTimeMillis()
                val statisticsOutput = statisticsInteractor.getStatsForUser(user = user, now = now)
                _screenViewState.emit(
                    when (statisticsOutput) {
                        is Output.Success -> {
                            mapper.map(
                                showUsersSwitch = moreThanOneUser,
                                userStats = statisticsOutput.result,
                            )
                        }
                        is Output.Error -> {
                            StatisticsViewState.Error(
                                errorCode = statisticsOutput.errorCode.code,
                                user = user,
                                showUsersSwitch = moreThanOneUser,
                            )
                        }
                    },
                )
                _dialogViewState.emit(StatisticsDialog.None)
            }
        }

        fun openPickUser() {
            viewModelScope.launch(context = mainDispatcher) {
                val users = currentUsers
                if (users == null) {
                    logger.e("StatisticsViewModel", "openPickUser called but currentUsers is null")
                    return@launch
                }

                if (users.size < 2) {
                    logger.e(
                        "StatisticsViewModel",
                        "openPickUser called but there is only 1 user or less",
                    )
                }
                _dialogViewState.emit(StatisticsDialog.SelectUser(users.toList()))
            }
        }

        fun deleteAllSessionsForUser(user: User) {
            logger.d(
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
