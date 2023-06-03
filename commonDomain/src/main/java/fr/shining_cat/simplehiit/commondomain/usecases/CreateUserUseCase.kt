package fr.shining_cat.simplehiit.commondomain.usecases

import fr.shining_cat.simplehiit.commondomain.Constants
import fr.shining_cat.simplehiit.commondomain.Output
import fr.shining_cat.simplehiit.commondomain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.commondomain.models.User
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.commonutils.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val checkIfAnotherUserUsesThatNameUseCase: CheckIfAnotherUserUsesThatNameUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(user: User): Output<Long> {
        return withContext(defaultDispatcher) {
            val anotherUserUsesThatNameOutput = checkIfAnotherUserUsesThatNameUseCase.execute(user)
            when (anotherUserUsesThatNameOutput) {
                is Output.Error -> anotherUserUsesThatNameOutput
                is Output.Success -> {
                    if (anotherUserUsesThatNameOutput.result) {
                        val nameTakenException = Exception(Constants.Errors.USER_NAME_TAKEN.code)
                        Output.Error(Constants.Errors.USER_NAME_TAKEN, nameTakenException)
                    } else {
                        simpleHiitRepository.insertUser(user)
                    }
                }
            }
        }
    }
}