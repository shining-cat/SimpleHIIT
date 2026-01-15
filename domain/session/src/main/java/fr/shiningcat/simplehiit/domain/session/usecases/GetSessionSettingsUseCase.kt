/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.session.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import fr.shiningcat.simplehiit.domain.common.datainterfaces.UsersRepository
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.SessionSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform

class GetSessionSettingsUseCase(
    private val usersRepository: UsersRepository,
    private val settingsRepository: SettingsRepository,
    private val logger: HiitLogger,
) {
    fun execute(): Flow<Output<SessionSettings>> {
        val usersFlow = usersRepository.getSelectedUsers()
        val settingsFlow = settingsRepository.getPreferences()
        return usersFlow.combineTransform(settingsFlow) { usersOutput, settings ->
            if (usersOutput is Output.Error) {
                val exception = usersOutput.exception
                logger.e("GetGeneralSettingsUseCase", "Error retrieving users", exception)
                emit(Output.Error(DomainError.NO_USERS_FOUND, exception))
            } else {
                usersOutput as Output.Success
                val totalCycleLength =
                    (settings.workPeriodLengthMs + settings.restPeriodLengthMs) * settings.numberOfWorkPeriods
                emit(
                    Output.Success(
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
                        ),
                    ),
                )
            }
        }
    }
}
