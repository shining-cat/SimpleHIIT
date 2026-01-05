package fr.shiningcat.simplehiit.sharedui.statistics

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.NonEmptyList
import fr.shiningcat.simplehiit.commonutils.TimeProvider
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * Pure Kotlin presenter containing all business logic for the Statistics screen.
 * No Android framework dependencies - fully unit testable.
 */
class StatisticsPresenter(
    private val statisticsInteractor: StatisticsInteractor,
    private val mapper: StatisticsViewStateMapper,
    private val timeProvider: TimeProvider,
    private val logger: HiitLogger,
) {
    // Cache the current users list for use in retrieveStatsForUser and openPickUser
    // Type-safe: NonEmptyList guarantees at least one user
    private var currentUsers: NonEmptyList<User>? = null

    private val _screenViewState =
        MutableStateFlow<StatisticsViewState>(StatisticsViewState.Loading)

    private val _dialogState = MutableStateFlow<StatisticsDialog>(StatisticsDialog.None)

    /**
     * The screen view state - reactive flow of statistics data.
     * Statistics are loaded on-demand per user, not a continuous reactive stream.
     */
    val screenViewState: Flow<StatisticsViewState> = _screenViewState.asStateFlow()

    /**
     * The dialog state - UI-driven state managed manually for dialogs and one-time events.
     */
    val dialogState: Flow<StatisticsDialog> = _dialogState.asStateFlow()

    /**
     * Observes users and returns a flow that emits screen states.
     * This should be collected by the ViewModel to drive updates.
     */
    fun observeUsers(): Flow<Unit> =
        statisticsInteractor.getAllUsers().map { usersOutput ->
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

    /**
     * Retrieves and displays statistics for the specified user.
     *
     * @param user The user to retrieve statistics for
     */
    suspend fun retrieveStatsForUser(user: User) {
        logger.d("StatisticsPresenter", "retrieveStatsForUser::user = $user")

        val users = currentUsers
        if (users == null) {
            logger.e("StatisticsPresenter", "retrieveStatsForUser called but currentUsers is null")
            return
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
        _dialogState.emit(StatisticsDialog.None)
    }

    /**
     * Opens the user picker dialog.
     */
    suspend fun openPickUser() {
        val users = currentUsers
        if (users == null) {
            logger.e("StatisticsPresenter", "openPickUser called but currentUsers is null")
            return
        }

        if (users.size < 2) {
            logger.e(
                "StatisticsPresenter",
                "openPickUser called but there is only 1 user or less",
            )
        }
        _dialogState.emit(StatisticsDialog.SelectUser(users.toList()))
    }

    /**
     * Shows confirmation dialog for deleting all sessions for a user.
     *
     * @param user The user whose sessions will be deleted
     */
    suspend fun deleteAllSessionsForUser(user: User) {
        logger.d(
            "StatisticsPresenter",
            "deleteAllSessionsForUser::user = $user",
        )
        _dialogState.emit(
            StatisticsDialog.ConfirmDeleteAllSessionsForUser(user),
        )
    }

    /**
     * Executes deletion of all sessions for a user after confirmation.
     *
     * @param user The user whose sessions will be deleted
     */
    suspend fun deleteAllSessionsForUserConfirmation(user: User) {
        statisticsInteractor.deleteSessionsForUser(user.id)
        _dialogState.emit(StatisticsDialog.None)
        // Force refresh:
        retrieveStatsForUser(user = user)
    }

    /**
     * Shows the confirmation dialog for resetting the whole app.
     */
    suspend fun resetWholeApp() {
        _dialogState.emit(StatisticsDialog.ConfirmWholeReset)
    }

    /**
     * Executes the whole app reset after confirmation.
     */
    suspend fun resetWholeAppConfirmation() {
        statisticsInteractor.resetWholeApp()
    }

    /**
     * Dismisses any currently shown dialog.
     */
    suspend fun cancelDialog() {
        _dialogState.emit(StatisticsDialog.None)
    }
}
