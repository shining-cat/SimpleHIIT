package fr.shining_cat.simplehiit.domain.statistics

import fr.shining_cat.simplehiit.domain.common.Output
import fr.shining_cat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.commonutils.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteSessionsForUserUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(userId: Long): Output<Int> {
        return withContext(defaultDispatcher) {
            simpleHiitRepository.deleteSessionRecordsForUser(userId)
        }
    }
}