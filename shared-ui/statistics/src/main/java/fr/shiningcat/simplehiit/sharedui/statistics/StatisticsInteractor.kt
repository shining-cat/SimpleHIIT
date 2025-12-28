package fr.shiningcat.simplehiit.sharedui.statistics

import fr.shiningcat.simplehiit.commonutils.NonEmptyList
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.domain.common.models.UserStatistics
import fr.shiningcat.simplehiit.domain.common.usecases.ResetWholeAppUseCase
import fr.shiningcat.simplehiit.domain.statistics.usecases.DeleteSessionsForUserUseCase
import fr.shiningcat.simplehiit.domain.statistics.usecases.GetAllUsersUseCase
import fr.shiningcat.simplehiit.domain.statistics.usecases.GetStatsForUserUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface StatisticsInteractor {
    fun getAllUsers(): Flow<Output<NonEmptyList<User>>>

    suspend fun getStatsForUser(
        user: User,
        now: Long,
    ): Output<UserStatistics>

    suspend fun deleteSessionsForUser(userId: Long)

    suspend fun resetWholeApp()
}

class StatisticsInteractorImpl
    @Inject
    constructor(
        private val getAllUsersUseCase: GetAllUsersUseCase,
        private val getStatsForUserUseCase: GetStatsForUserUseCase,
        private val deleteSessionsForUserUseCase: DeleteSessionsForUserUseCase,
        private val resetWholeAppUseCase: ResetWholeAppUseCase,
    ) : StatisticsInteractor {
        override fun getAllUsers(): Flow<Output<NonEmptyList<User>>> = getAllUsersUseCase.execute()

        override suspend fun getStatsForUser(
            user: User,
            now: Long,
        ): Output<UserStatistics> = getStatsForUserUseCase.execute(user, now)

        override suspend fun deleteSessionsForUser(userId: Long) {
            deleteSessionsForUserUseCase.execute(userId)
        }

        override suspend fun resetWholeApp() = resetWholeAppUseCase.execute()
    }
