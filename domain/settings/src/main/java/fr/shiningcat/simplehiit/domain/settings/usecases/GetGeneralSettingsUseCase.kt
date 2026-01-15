/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import fr.shiningcat.simplehiit.domain.common.datainterfaces.UsersRepository
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.GeneralSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetGeneralSettingsUseCase(
    private val usersRepository: UsersRepository,
    private val settingsRepository: SettingsRepository,
    private val getCurrentAppLanguageUseCase: GetCurrentAppLanguageUseCase,
    private val logger: HiitLogger,
) {
    fun execute(): Flow<Output<GeneralSettings>> {
        val usersFlow = usersRepository.getUsers()
        val settingsFlow = settingsRepository.getPreferences()
        val languageFlow = getCurrentAppLanguageUseCase.execute()
        return combine(usersFlow, settingsFlow, languageFlow) { usersOutput, settings, currentLanguage ->
            if (usersOutput is Output.Error) {
                val exception = usersOutput.exception
                logger.e("GetGeneralSettingsUseCase", "Error retrieving users", exception)
                Output.Error(
                    DomainError.NO_USERS_FOUND,
                    exception,
                )
            } else {
                usersOutput as Output.Success
                val totalCycleLength =
                    settings.numberOfWorkPeriods.times((settings.workPeriodLengthMs.plus(settings.restPeriodLengthMs)))
                Output.Success(
                    GeneralSettings(
                        workPeriodLengthMs = settings.workPeriodLengthMs,
                        restPeriodLengthMs = settings.restPeriodLengthMs,
                        numberOfWorkPeriods = settings.numberOfWorkPeriods,
                        cycleLengthMs = totalCycleLength,
                        beepSoundCountDownActive = settings.beepSoundActive,
                        sessionStartCountDownLengthMs = settings.sessionCountDownLengthMs,
                        periodsStartCountDownLengthMs = settings.PeriodCountDownLengthMs,
                        users = usersOutput.result,
                        exerciseTypes = settings.selectedExercisesTypes,
                        currentLanguage = currentLanguage,
                        currentTheme = settings.appTheme,
                    ),
                )
            }
        }
    }
}
