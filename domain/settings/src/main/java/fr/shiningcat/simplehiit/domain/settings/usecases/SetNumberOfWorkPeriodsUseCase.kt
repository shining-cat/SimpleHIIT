package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.di.DefaultDispatcher
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetNumberOfWorkPeriodsUseCase
    @Inject
    constructor(
        private val settingsRepository: SettingsRepository,
        @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
        private val simpleHiitLogger: HiitLogger,
    ) {
        suspend fun execute(number: Int) {
            withContext(defaultDispatcher) {
                settingsRepository.setNumberOfWorkPeriods(number)
            }
        }
    }
