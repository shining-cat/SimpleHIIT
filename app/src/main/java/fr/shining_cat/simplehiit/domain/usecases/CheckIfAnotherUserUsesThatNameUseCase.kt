package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.di.DefaultDispatcher
import fr.shining_cat.simplehiit.di.IoDispatcher
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckIfAnotherUserUsesThatNameUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(user: User): Output<Boolean> {
        return withContext(defaultDispatcher) {
            val existingUsers = simpleHiitRepository.getUsersList()
            when (existingUsers) {
                is Output.Success -> {
                    if (existingUsers.result.find { it.name == user.name && it.id != user.id } != null) {
                        Output.Success(true)
                    } else {
                        Output.Success(false)
                    }
                }
                is Output.Error -> existingUsers
            }
        }
    }
}