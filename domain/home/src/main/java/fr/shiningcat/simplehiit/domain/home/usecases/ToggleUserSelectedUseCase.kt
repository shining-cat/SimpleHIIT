package fr.shiningcat.simplehiit.domain.home.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.UsersRepository
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ToggleUserSelectedUseCase(
    private val usersRepository: UsersRepository,
    private val defaultDispatcher: CoroutineDispatcher,
    private val logger: HiitLogger,
) {
    suspend fun execute(user: User): Output<Int> =
        withContext(defaultDispatcher) {
            logger.d("ToggleUserSelectedUseCase", "execute::user = $user")
            usersRepository.updateUser(user)
        }
}
