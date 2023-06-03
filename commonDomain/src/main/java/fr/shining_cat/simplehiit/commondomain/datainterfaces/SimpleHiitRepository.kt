package fr.shining_cat.simplehiit.commondomain.datainterfaces

import fr.shining_cat.simplehiit.commondomain.Output
import fr.shining_cat.simplehiit.commondomain.models.ExerciseType
import fr.shining_cat.simplehiit.commondomain.models.SessionRecord
import fr.shining_cat.simplehiit.commondomain.models.SimpleHiitPreferences
import fr.shining_cat.simplehiit.commondomain.models.User
import fr.shining_cat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport
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