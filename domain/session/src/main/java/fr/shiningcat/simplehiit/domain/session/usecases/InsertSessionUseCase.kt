package fr.shiningcat.simplehiit.domain.session.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.di.DefaultDispatcher
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SessionsRepository
import fr.shiningcat.simplehiit.domain.common.models.SessionRecord
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InsertSessionUseCase
    @Inject
    constructor(
        private val sessionsRepository: SessionsRepository,
        @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
        private val logger: HiitLogger,
    ) {
        suspend fun execute(sessionRecord: SessionRecord): Output<Int> =
            withContext(defaultDispatcher) {
                sessionsRepository.insertSessionRecord(sessionRecord)
            }
    }
