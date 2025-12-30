package fr.shiningcat.simplehiit.domain.home.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SetTotalRepetitionsNumberUseCase(
    private val settingsRepository: SettingsRepository,
    private val defaultDispatcher: CoroutineDispatcher,
    private val logger: HiitLogger,
) {
    suspend fun execute(number: Int) {
        withContext(defaultDispatcher) {
            settingsRepository.setTotalRepetitionsNumber(number)
        }
    }
}
