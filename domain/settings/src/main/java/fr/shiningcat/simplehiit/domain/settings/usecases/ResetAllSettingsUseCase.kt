package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ResetAllSettingsUseCase(
    private val settingsRepository: SettingsRepository,
    private val defaultDispatcher: CoroutineDispatcher,
    private val logger: HiitLogger,
) {
    suspend fun execute() {
        withContext(defaultDispatcher) {
            settingsRepository.resetAllSettings()
        }
    }
}
