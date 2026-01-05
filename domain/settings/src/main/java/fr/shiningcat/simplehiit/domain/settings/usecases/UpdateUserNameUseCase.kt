package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.UsersRepository
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateUserNameUseCase(
    private val usersRepository: UsersRepository,
    private val checkIfAnotherUserUsesThatNameUseCase: CheckIfAnotherUserUsesThatNameUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    private val logger: HiitLogger,
) {
    suspend fun execute(user: User): Output<Int> =
        withContext(defaultDispatcher) {
            val anotherUserUsesThatNameOutput = checkIfAnotherUserUsesThatNameUseCase.execute(user)
            when (anotherUserUsesThatNameOutput) {
                is Output.Error -> {
                    anotherUserUsesThatNameOutput
                }
                is Output.Success -> {
                    if (anotherUserUsesThatNameOutput.result) {
                        val nameTakenException =
                            Exception(DomainError.USER_NAME_TAKEN.code)
                        Output.Error(
                            DomainError.USER_NAME_TAKEN,
                            nameTakenException,
                        )
                    } else {
                        usersRepository.updateUser(user)
                    }
                }
            }
        }
}
