package fr.shining_cat.simplehiit.domain.datainterfaces

import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.*
import kotlinx.coroutines.flow.Flow

@ExcludeFromJacocoGeneratedReport
interface SimpleHiitRepository {

    suspend fun insertUser(user: User): Output<Long>
    fun getUsers(): Flow<Output<List<User>>>
    fun getSelectedUsers(): Flow<Output<List<User>>>
    suspend fun updateUser(user: User): Output<Int>
    suspend fun deleteUser(user: User): Output<Int>

    //
    suspend fun insertSession(session: Session): Output<Int>
    suspend fun getSessionsForUser(user: User): Output<List<Session>>

    //
    fun getGeneralSettings(): Flow<SimpleHiitSettings>
    fun getTotalRepetitionsSetting(): Flow<TotalRepetitionsSetting>

    //
    suspend fun setWorkPeriodLength(durationSeconds: Int)
    suspend fun setRestPeriodLength(durationSeconds: Int)
    suspend fun setNumberOfWorkPeriods(number: Int)
    suspend fun setBeepSound(active: Boolean)
    suspend fun setSessionStartCountdown(durationSeconds: Int)
    suspend fun setPeriodStartCountdown(durationSeconds: Int)
    suspend fun setTotalRepetitionsNumber(number: Int)
    suspend fun setExercisesTypesSelected(exercisesTypes: List<ExerciseType>)
    suspend fun resetAllSettings()

}