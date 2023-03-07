package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val checkIfAnotherUserUsesThatNameUseCase: CheckIfAnotherUserUsesThatNameUseCase,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(user: User): Output<Long> {
        val anotherUserUsesThatNameOutput = checkIfAnotherUserUsesThatNameUseCase.execute(user)
        return when (anotherUserUsesThatNameOutput) {
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