package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.di.DefaultDispatcher
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.UsersRepository
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateUserNameUseCase
    @Inject
    constructor(
        private val usersRepository: UsersRepository,
        private val checkIfAnotherUserUsesThatNameUseCase: CheckIfAnotherUserUsesThatNameUseCase,
        @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
        private val simpleHiitLogger: HiitLogger,
    ) {
        suspend fun execute(user: User): Output<Int> =
            withContext(defaultDispatcher) {
                val anotherUserUsesThatNameOutput = checkIfAnotherUserUsesThatNameUseCase.execute(user)
                when (anotherUserUsesThatNameOutput) {
                    is Output.Error -> anotherUserUsesThatNameOutput
                    is Output.Success -> {
                        if (anotherUserUsesThatNameOutput.result) {
                            val nameTakenException =
                                Exception(Constants.Errors.USER_NAME_TAKEN.code)
                            Output.Error(
                                Constants.Errors.USER_NAME_TAKEN,
                                nameTakenException,
                            )
                        } else {
                            usersRepository.updateUser(user)
                        }
                    }
                }
            }
    }
