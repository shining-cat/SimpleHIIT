package fr.shiningcat.simplehiit.domain.statistics.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SessionsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DeleteSessionsForUserUseCase(
    private val sessionsRepository: SessionsRepository,
    private val defaultDispatcher: CoroutineDispatcher,
    private val simpleHiitLogger: HiitLogger,
) {
    suspend fun execute(userId: Long): Output<Int> =
        withContext(defaultDispatcher) {
            sessionsRepository.deleteSessionRecordsForUser(userId)
        }
}
