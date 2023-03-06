package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class UpdateUserNameUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val checkUserNameFreeUseCase: CheckUserNameFreeUseCase,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(user: User): Output<Int> {
        val nameIsFreeCheckOutput = checkUserNameFreeUseCase.execute(user.name)
        return when (nameIsFreeCheckOutput) {
            is Output.Error -> nameIsFreeCheckOutput
            is Output.Success -> {
                if (nameIsFreeCheckOutput.result) {
                    simpleHiitRepository.updateUser(user)
                } else {
                    val nameTakenException = Exception(Constants.Errors.USER_NAME_TAKEN.code)
                    Output.Error(Constants.Errors.USER_NAME_TAKEN, nameTakenException)
                }
            }
        }
    }
}