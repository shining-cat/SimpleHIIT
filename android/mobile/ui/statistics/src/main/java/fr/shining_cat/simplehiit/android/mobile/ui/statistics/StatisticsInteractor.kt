package fr.shining_cat.simplehiit.android.mobile.ui.statistics

import fr.shining_cat.simplehiit.domain.common.Output
import fr.shining_cat.simplehiit.domain.common.models.User
import fr.shining_cat.simplehiit.domain.common.models.UserStatistics
import fr.shining_cat.simplehiit.domain.common.usecases.ResetWholeAppUseCase
import fr.shining_cat.simplehiit.domain.statistics.usecases.DeleteSessionsForUserUseCase
import fr.shining_cat.simplehiit.domain.statistics.usecases.GetAllUsersUseCase
import fr.shining_cat.simplehiit.domain.statistics.usecases.GetStatsForUserUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface StatisticsInteractor {
    fun getAllUsers(): Flow<Output<List<User>>>
    suspend fun getStatsForUser(user: User, now: Long): Output<UserStatistics>
    suspend fun deleteSessionsForUser(userId: Long): Output<Int>
    suspend fun resetWholeApp()
}

class StatisticsInteractorImpl @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getStatsForUserUseCase: GetStatsForUserUseCase,
    private val deleteSessionsForUserUseCase: DeleteSessionsForUserUseCase,
    private val resetWholeAppUseCase: ResetWholeAppUseCase
) : StatisticsInteractor {
    override fun getAllUsers(): Flow<Output<List<User>>> {
        return getAllUsersUseCase.execute()
    }

    override suspend fun getStatsForUser(user: User, now: Long): Output<UserStatistics> {
        return getStatsForUserUseCase.execute(user, now)
    }

    override suspend fun deleteSessionsForUser(userId: Long): Output<Int> {
        return deleteSessionsForUserUseCase.execute(userId)
    }

    override suspend fun resetWholeApp() {
        return resetWholeAppUseCase.execute()
    }

}