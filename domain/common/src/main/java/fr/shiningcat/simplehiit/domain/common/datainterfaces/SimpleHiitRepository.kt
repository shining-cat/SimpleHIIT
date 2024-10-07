package fr.shiningcat.simplehiit.domain.common.datainterfaces

import fr.shiningcat.simplehiit.commonutils.annotations.ExcludeFromJacocoGeneratedReport
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.SessionRecord
import fr.shiningcat.simplehiit.domain.common.models.SimpleHiitPreferences
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.flow.Flow

@ExcludeFromJacocoGeneratedReport
interface SimpleHiitRepository {
    suspend fun insertUser(user: User): Output<Long>

    fun getUsers(): Flow<Output<List<User>>>

    suspend fun getUsersList(): Output<List<User>>

    fun getSelectedUsers(): Flow<Output<List<User>>>

    suspend fun updateUser(user: User): Output<Int>

    suspend fun deleteUser(user: User): Output<Int>

    suspend fun deleteAllUsers()

    //
    suspend fun insertSessionRecord(sessionRecord: SessionRecord): Output<Int>

    suspend fun getSessionRecordsForUser(user: User): Output<List<SessionRecord>>

    suspend fun deleteSessionRecordsForUser(userId: Long): Output<Int>

    //
    fun getPreferences(): Flow<SimpleHiitPreferences>

    //
    suspend fun setWorkPeriodLength(durationMs: Long)

    suspend fun setRestPeriodLength(durationMs: Long)

    suspend fun setNumberOfWorkPeriods(number: Int)

    suspend fun setBeepSound(active: Boolean)

    suspend fun setSessionStartCountdown(durationMs: Long)

    suspend fun setPeriodStartCountdown(durationMs: Long)

    suspend fun setTotalRepetitionsNumber(number: Int)

    suspend fun setExercisesTypesSelected(exercisesTypes: List<ExerciseType>)

    suspend fun resetAllSettings()
}
