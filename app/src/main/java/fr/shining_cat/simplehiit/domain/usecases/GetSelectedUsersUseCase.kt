package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Constants.NO_RESULTS_FOUND
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSelectedUsersUseCase(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val simpleHiitLogger: HiitLogger
) {

    fun execute(): Flow<Output<List<User>>> {
        val selectedUsersFlow = simpleHiitRepository.getSelectedUsers().map {
            when(it){
                is Output.Success -> if(it.result.isEmpty()){
                    simpleHiitLogger.e("GetSelectedUsersUseCase", "No selected users found")
                    val emptyResultException = Exception(NO_RESULTS_FOUND)
                    Output.Error(errorCode = Constants.Errors.EMPTY_RESULT, exception = emptyResultException)
                } else{
                    it
                }
                is Output.Error -> it
            }
        }
        return selectedUsersFlow
    }
}