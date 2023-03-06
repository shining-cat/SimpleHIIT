package fr.shining_cat.simplehiit.domain.datainterfaces

import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.*
import kotlinx.coroutines.flow.Flow

@ExcludeFromJacocoGeneratedReport
interface SimpleHiitRepository {

    suspend fun insertUser(user: User): Output<Long>
    fun getUsers(): Flow<Output<List<User>>>
    suspend fun getUsersList(): Output<List<User>>
    fun getSelectedUsers(): Flow<Output<List<User>>>
    suspend fun updateUser(user: User): Output<Int>
    suspend fun deleteUser(user: User): Output<Int>
    suspend fun deleteAllUsers(): Unit

    //
    suspend fun insertSession(session: Session): Output<Int>
    suspend fun getSessionsForUser(user: User): Output<List<Session>>
    suspend fun deleteSessionsForUser(userId: Long): Output<Int>

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