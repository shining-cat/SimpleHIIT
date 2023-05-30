package fr.shining_cat.simplehiit.commondomain.usecases

import fr.shining_cat.simplehiit.commondomain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.commonutils.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ResetWholeAppUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute() {
        withContext(defaultDispatcher) {
            simpleHiitRepository.resetAllSettings()
            simpleHiitRepository.deleteAllUsers()
        }
    }
}