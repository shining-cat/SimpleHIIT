package fr.shiningcat.simplehiit.domain.statistics.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.NonEmptyList
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Constants.NO_RESULTS_FOUND
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.UsersRepository
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllUsersUseCase
    @Inject
    constructor(
        private val usersRepository: UsersRepository,
        private val logger: HiitLogger,
    ) {
        fun execute(): Flow<Output<NonEmptyList<User>>> {
            val allUsersFlow =
                usersRepository.getUsers().map {
                    when (it) {
                        is Output.Success -> {
                            val nonEmptyList = NonEmptyList.fromList(it.result)
                            if (nonEmptyList == null) {
                                logger.e("GetAllUsersUseCase", "No users found")
                                val emptyResultException = Exception(NO_RESULTS_FOUND)
                                Output.Error(
                                    errorCode = Constants.Errors.NO_USERS_FOUND,
                                    exception = emptyResultException,
                                )
                            } else {
                                Output.Success(nonEmptyList)
                            }
                        }
                        is Output.Error -> {
                            it
                        }
                    }
                }
            return allUsersFlow
        }
    }
