package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class CheckUserNameFreeUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(name: String): Output<Boolean> {
        val existingUsers = simpleHiitRepository.getUsersList()
        return when (existingUsers) {
            is Output.Success -> {
                if (existingUsers.result.find { it.name == name } != null) {
                    Output.Success(false)
                } else {
                    Output.Success(true)
                }
            }
            is Output.Error -> existingUsers
        }
    }
}