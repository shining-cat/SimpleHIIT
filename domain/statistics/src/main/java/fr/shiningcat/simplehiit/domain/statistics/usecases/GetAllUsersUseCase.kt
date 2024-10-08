package fr.shiningcat.simplehiit.domain.statistics.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Constants.NO_RESULTS_FOUND
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllUsersUseCase
    @Inject
    constructor(
        private val simpleHiitRepository: SimpleHiitRepository,
        private val simpleHiitLogger: HiitLogger,
    ) {
        fun execute(): Flow<Output<List<User>>> {
            val allUsersFlow =
                simpleHiitRepository.getUsers().map {
                    when (it) {
                        is Output.Success ->
                            if (it.result.isEmpty()) {
                                simpleHiitLogger.e("GetAllUsersUseCase", "No users found")
                                val emptyResultException = Exception(NO_RESULTS_FOUND)
                                Output.Error(
                                    errorCode = Constants.Errors.NO_USERS_FOUND,
                                    exception = emptyResultException,
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
