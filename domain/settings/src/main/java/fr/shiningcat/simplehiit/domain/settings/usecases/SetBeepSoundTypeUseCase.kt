/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import fr.shiningcat.simplehiit.domain.common.models.BeepSoundType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SetBeepSoundTypeUseCase(
    private val settingsRepository: SettingsRepository,
    private val defaultDispatcher: CoroutineDispatcher,
    private val logger: HiitLogger,
) {
    suspend fun execute(beepSoundType: BeepSoundType) {
        withContext(defaultDispatcher) {
            settingsRepository.setBeepSoundType(beepSoundType)
        }
    }
}
