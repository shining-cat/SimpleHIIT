package fr.shiningcat.simplehiit.domain.session.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.UsersRepository

/**
 * Updates the last session timestamp for a list of user IDs.
 *
 * @param userIds List of user IDs to update
 * @param timestamp The timestamp to set as the last session time
 */
class UpdateUsersLastSessionTimestampUseCase(
    private val usersRepository: UsersRepository,
    private val logger: HiitLogger,
) {
    suspend fun execute(
        userIds: List<Long>,
        timestamp: Long,
    ): Output<Int> {
        logger.d("UpdateUsersLastSessionTimestampUseCase", "Updating last session timestamp for ${userIds.size} users")

        if (userIds.isEmpty()) {
            logger.e("UpdateUsersLastSessionTimestampUseCase", "No user IDs provided")
            return Output.Success(0)
        }

        when (val usersOutput = usersRepository.getUsersList()) {
            is Output.Success -> {
                val usersToUpdate = usersOutput.result.filter { it.id in userIds }
                var updatedCount = 0

                usersToUpdate.forEach { user ->
                    val updatedUser = user.copy(lastSessionTimestamp = timestamp)
                    when (usersRepository.updateUser(updatedUser)) {
                        is Output.Success -> {
                            updatedCount++
                            logger.d(
                                "UpdateUsersLastSessionTimestampUseCase",
                                "Updated last session timestamp for user ${user.id}",
                            )
                        }
                        is Output.Error -> {
                            logger.e(
                                "UpdateUsersLastSessionTimestampUseCase",
                                "Failed to update last session timestamp for user ${user.id}",
                            )
                        }
                    }
                }

                return Output.Success(updatedCount)
            }
            is Output.Error -> {
                logger.e(
                    "UpdateUsersLastSessionTimestampUseCase",
                    "Failed to fetch users list",
                )
                return Output.Error(usersOutput.errorCode, usersOutput.exception)
            }
        }
    }
}
