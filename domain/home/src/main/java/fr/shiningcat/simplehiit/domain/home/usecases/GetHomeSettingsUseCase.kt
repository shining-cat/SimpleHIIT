package fr.shiningcat.simplehiit.domain.home.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shiningcat.simplehiit.domain.common.models.HomeSettings
import fr.shiningcat.simplehiit.domain.common.models.SessionSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import javax.inject.Inject

class GetHomeSettingsUseCase
    @Inject
    constructor(
        private val simpleHiitRepository: SimpleHiitRepository,
        private val detectSessionWarningUseCase: DetectSessionWarningUseCase,
        private val simpleHiitLogger: HiitLogger,
    ) {
        fun execute(): Flow<Output<HomeSettings>> {
            var firstTogglingAttempt = true
            val usersFlow = simpleHiitRepository.getUsers()
            val settingsFlow = simpleHiitRepository.getPreferences()
            return usersFlow.combineTransform(settingsFlow) { usersOutput, settings ->
                when (usersOutput) {
                    is Output.Error -> {
                        val exception = usersOutput.exception
                        simpleHiitLogger.e(
                            "GetHomeSettingsUseCase",
                            "Error retrieving users",
                            exception,
                        )
                        emit(Output.Error(Constants.Errors.NO_USERS_FOUND, exception))
                    }

                    else -> {
                        usersOutput as Output.Success
                        if (usersOutput.result.size == 1 && !usersOutput.result[0].selected) {
                            // there is only 1 user in the DB, and it is not selected (edge case)
                            // => we automatically toggle this user to selected and continue
                            if (firstTogglingAttempt) {
                                // we only want this toggle attempt to be done once, if it has failed, something wrong is going on
                                firstTogglingAttempt = false
                                val toggleUniqueUserToSelected =
                                    usersOutput.result[0].copy(selected = true)
                                val togglingUser =
                                    simpleHiitRepository.updateUser(toggleUniqueUserToSelected)
                                when (togglingUser) {
                                    is Output.Error -> {
                                        simpleHiitLogger.e(
                                            "GetHomeSettingsUseCase",
                                            "Error toggling the only existing user to selected",
                                            togglingUser.exception,
                                        )
                                        emit(
                                            Output.Error(
                                                Constants.Errors.DATABASE_UPDATE_FAILED,
                                                togglingUser.exception,
                                            ),
                                        )
                                    }

                                    is Output.Success -> {
                                        if (togglingUser.result != 1) {
                                            val failedTogglingUser =
                                                Exception(
                                                    Constants.Errors.DATABASE_UPDATE_FAILED.code,
                                                )
                                            simpleHiitLogger.e(
                                                "GetHomeSettingsUseCase",
                                                "Error toggling the only existing user to selected",
                                                failedTogglingUser,
                                            )
                                            emit(
                                                Output.Error(
                                                    Constants.Errors.DATABASE_UPDATE_FAILED,
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
                                        Constants.Errors.NO_SELECTED_USERS_FOUND.code,
                                    )
                                simpleHiitLogger.e(
                                    "GetHomeSettingsUseCase",
                                    "Error retrieving selected users: only 1 user found and is not selected after trying to toggle it",
                                    couldNotFindAnySelectedUser,
                                )
                                emit(
                                    Output.Error(
                                        Constants.Errors.NO_SELECTED_USERS_FOUND,
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
                                    users = usersOutput.result,
                                    exerciseTypes = settings.selectedExercisesTypes,
                                )
                            val warning = detectSessionWarningUseCase.execute(sessionSettings)

                            emit(
                                Output.Success(
                                    HomeSettings(
                                        numberCumulatedCycles = settings.numberCumulatedCycles,
                                        cycleLengthMs = totalCycleLength,
                                        users = usersOutput.result,
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
