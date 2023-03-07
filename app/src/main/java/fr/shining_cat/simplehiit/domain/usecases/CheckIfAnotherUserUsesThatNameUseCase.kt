package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class CheckIfAnotherUserUsesThatNameUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(user: User): Output<Boolean> {
        val existingUsers = simpleHiitRepository.getUsersList()
        return when (existingUsers) {
            is Output.Success -> {
                if (existingUsers.result.find { it.name == user.name && it.id != user.id} != null) {
                    Output.Success(true)
                } else {
                    Output.Success(false)
                }
            }
            is Output.Error -> existingUsers
        }
    }
}