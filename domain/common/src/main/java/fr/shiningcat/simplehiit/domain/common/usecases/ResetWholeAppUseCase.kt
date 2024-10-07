package fr.shiningcat.simplehiit.domain.common.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.di.DefaultDispatcher
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ResetWholeAppUseCase
    @Inject
    constructor(
        private val simpleHiitRepository: SimpleHiitRepository,
        @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
        private val simpleHiitLogger: HiitLogger,
    ) {
        suspend fun execute() {
            withContext(defaultDispatcher) {
                simpleHiitRepository.resetAllSettings()
                simpleHiitRepository.deleteAllUsers()
            }
        }
    }
