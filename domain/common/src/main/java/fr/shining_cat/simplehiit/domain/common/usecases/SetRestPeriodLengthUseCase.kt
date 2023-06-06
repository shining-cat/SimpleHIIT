package fr.shining_cat.simplehiit.domain.common.usecases

import fr.shining_cat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.commonutils.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetRestPeriodLengthUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(durationMs: Long) {
        withContext(defaultDispatcher) {
            simpleHiitRepository.setRestPeriodLength(durationMs)
        }
    }
}