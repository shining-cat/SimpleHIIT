package fr.shining_cat.simplehiit.commondomain.usecases

import fr.shining_cat.simplehiit.commondomain.Constants
import fr.shining_cat.simplehiit.commondomain.Constants.NO_RESULTS_FOUND
import fr.shining_cat.simplehiit.commondomain.Output
import fr.shining_cat.simplehiit.commondomain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.commondomain.models.User
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val simpleHiitLogger: HiitLogger
) {

    fun execute(): Flow<Output<List<User>>> {
        val allUsersFlow = simpleHiitRepository.getUsers().map {
            when (it) {
                is Output.Success -> if (it.result.isEmpty()) {
                    simpleHiitLogger.e("GetAllUsersUseCase", "No users found")
                    val emptyResultException = Exception(NO_RESULTS_FOUND)
                    Output.Error(
                        errorCode = Constants.Errors.NO_USERS_FOUND,
                        exception = emptyResultException
                    )
                } else {
                    it
                }
                is Output.Error -> it
            }
        }
        return allUsersFlow
    }
}