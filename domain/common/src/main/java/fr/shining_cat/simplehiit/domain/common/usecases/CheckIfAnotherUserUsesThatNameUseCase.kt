package fr.shining_cat.simplehiit.domain.common.usecases

import fr.shining_cat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.common.models.User
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.commonutils.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckIfAnotherUserUsesThatNameUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(user: User): fr.shining_cat.simplehiit.domain.common.Output<Boolean> {
        return withContext(defaultDispatcher) {
            val existingUsers = simpleHiitRepository.getUsersList()
            when (existingUsers) {
                is fr.shining_cat.simplehiit.domain.common.Output.Success -> {
                    if (existingUsers.result.find { it.name == user.name && it.id != user.id } != null) {
                        fr.shining_cat.simplehiit.domain.common.Output.Success(true)
                    } else {
                        fr.shining_cat.simplehiit.domain.common.Output.Success(false)
                    }
                }

                is fr.shining_cat.simplehiit.domain.common.Output.Error -> existingUsers
            }
        }
    }
}