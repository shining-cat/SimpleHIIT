package fr.shiningcat.simplehiit.domain.home.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import fr.shiningcat.simplehiit.domain.common.datainterfaces.UsersRepository
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.HomeSettings
import fr.shiningcat.simplehiit.domain.common.models.SessionSettings
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform

class GetHomeSettingsUseCase(
    private val usersRepository: UsersRepository,
    private val settingsRepository: SettingsRepository,
    private val detectSessionWarningUseCase: DetectSessionWarningUseCase,
    private val logger: HiitLogger,
) {
    /**
     * Sorts users by their last session timestamp, with most recent first.
     * Users with no session (null timestamp) appear last, sorted by ID.
     */
    private fun sortUsersByLastSession(users: List<User>) =
        users.sortedWith(
            compareByDescending<User> { it.lastSessionTimestamp != null }
                .thenByDescending { it.lastSessionTimestamp }
                .thenBy { it.id },
        )

    fun execute(): Flow<Output<HomeSettings>> {
        var firstTogglingAttempt = true
        val usersFlow = usersRepository.getUsers()
        val settingsFlow = settingsRepository.getPreferences()
        return usersFlow.combineTransform(settingsFlow) { usersOutput, settings ->
            when (usersOutput) {
                is Output.Error -> {
                    val exception = usersOutput.exception
                    logger.e(
                        "GetHomeSettingsUseCase",
                        "Error retrieving users",
                        exception,
                    )
                    emit(Output.Error(DomainError.NO_USERS_FOUND, exception))
                }
                else -> {
                    usersOutput as Output.Success
                    val sortedUsers = sortUsersByLastSession(usersOutput.result)
                    if (sortedUsers.size == 1 && !sortedUsers[0].selected) {
                        // there is only 1 user in the DB, and it is not selected (edge case)
                        // => we automatically toggle this user to selected and continue
                        if (firstTogglingAttempt) {
                            // we only want this toggle attempt to be done once, if it has failed, something wrong is going on
                            firstTogglingAttempt = false
                            val toggleUniqueUserToSelected =
                                sortedUsers[0].copy(selected = true)
                            val togglingUser =
                                usersRepository.updateUser(toggleUniqueUserToSelected)
                            when (togglingUser) {
                                is Output.Error -> {
                                    logger.e(
                                        "GetHomeSettingsUseCase",
                                        "Error toggling the only existing user to selected",
                                        togglingUser.exception,
                                    )
                                    emit(
                                        Output.Error(
                                            DomainError.DATABASE_UPDATE_FAILED,
                                            togglingUser.exception,
                                        ),
                                    )
                                }
                                is Output.Success -> {
                                    if (togglingUser.result != 1) {
                                        val failedTogglingUser =
                                            Exception(
                                                DomainError.DATABASE_UPDATE_FAILED.code,
                                            )
                                        logger.e(
                                            "GetHomeSettingsUseCase",
                                            "Error toggling the only existing user to selected",
                                            failedTogglingUser,
                                        )
                                        emit(
                                            Output.Error(
                                                DomainError.DATABASE_UPDATE_FAILED,
                                                failedTogglingUser,
                                            ),
                                        )
                                    }
                                    // else: unique user has successfully been toggled to selected, a new emission of simpleHiitRepository.getUsers is expected to be triggered by the toggling
                                }
                            }
                        } else {
                            val couldNotFindAnySelectedUser =
                                Exception(
                                    DomainError.NO_SELECTED_USERS_FOUND.code,
                                )
                            logger.e(
                                "GetHomeSettingsUseCase",
                                "Error retrieving selected users: only 1 user found and is not selected after trying to toggle it",
                                couldNotFindAnySelectedUser,
                            )
                            emit(
                                Output.Error(
                                    DomainError.NO_SELECTED_USERS_FOUND,
                                    couldNotFindAnySelectedUser,
                                ),
                            )
                        }
                    } else {
                        val totalCycleLength =
                            settings.numberOfWorkPeriods.times(
                                (settings.workPeriodLengthMs.plus(settings.restPeriodLengthMs)),
                            )

                        // Detect session warnings
                        val sessionSettings =
                            SessionSettings(
                                numberCumulatedCycles = settings.numberCumulatedCycles,
                                workPeriodLengthMs = settings.workPeriodLengthMs,
                                restPeriodLengthMs = settings.restPeriodLengthMs,
                                numberOfWorkPeriods = settings.numberOfWorkPeriods,
                                cycleLengthMs = totalCycleLength,
                                beepSoundCountDownActive = settings.beepSoundActive,
                                sessionStartCountDownLengthMs = settings.sessionCountDownLengthMs,
                                periodsStartCountDownLengthMs = settings.PeriodCountDownLengthMs,
                                users = sortedUsers,
                                exerciseTypes = settings.selectedExercisesTypes,
                            )
                        val warning = detectSessionWarningUseCase.execute(sessionSettings)

                        emit(
                            Output.Success(
                                HomeSettings(
                                    numberCumulatedCycles = settings.numberCumulatedCycles,
                                    cycleLengthMs = totalCycleLength,
                                    users = sortedUsers,
                                    warning = warning,
                                ),
                            ),
                        )
                    }
                }
            }
        }
    }
}
