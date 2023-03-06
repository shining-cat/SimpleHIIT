package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CheckUserNameFreeUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val simpleHiitLogger: HiitLogger
) {

suspend fun execute(name: String): Output<Boolean> {
    simpleHiitLogger.d("CheckUserNameFreeUseCase","execute::name = $name")
    val existingUsers = simpleHiitRepository.getUsers().first()
    return when (existingUsers) {
        is Output.Success -> {
            if (existingUsers.result.find { it.name == name } != null) {
                simpleHiitLogger.d("CheckUserNameFreeUseCase","name is taken")
                Output.Success(false)
            } else {
                simpleHiitLogger.d("CheckUserNameFreeUseCase","name is free")
                Output.Success(true)
            }
        }
        is Output.Error -> existingUsers
    }
}
}