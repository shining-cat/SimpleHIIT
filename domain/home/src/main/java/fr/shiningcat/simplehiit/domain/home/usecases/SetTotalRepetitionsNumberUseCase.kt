package fr.shiningcat.simplehiit.domain.home.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.di.DefaultDispatcher
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetTotalRepetitionsNumberUseCase
    @Inject
    constructor(
        private val settingsRepository: SettingsRepository,
        @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
        private val logger: HiitLogger,
    ) {
        suspend fun execute(number: Int) {
            withContext(defaultDispatcher) {
                settingsRepository.setTotalRepetitionsNumber(number)
            }
        }
    }
