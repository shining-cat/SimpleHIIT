package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.di.IoDispatcher
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ToggleUserSelectedUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(user: User): Output<Int> {
        return withContext(ioDispatcher) {
            simpleHiitLogger.d("ToggleUserSelectedUseCase", "execute::user = $user")
            simpleHiitRepository.updateUser(user)
        }
    }
}