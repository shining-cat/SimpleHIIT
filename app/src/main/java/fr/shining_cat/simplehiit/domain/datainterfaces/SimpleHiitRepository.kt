package fr.shining_cat.simplehiit.domain.datainterfaces

import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.domain.models.Session
import fr.shining_cat.simplehiit.domain.models.User

@ExcludeFromJacocoGeneratedReport
interface SimpleHiitRepository {

    suspend fun insertUser(user:User): Output<Long>
    suspend fun getUsers():Output<List<User>>
    suspend fun getSelectedUsers():Output<List<User>>
    suspend fun updateUser(user:User):Output<Int>
    suspend fun deleteUser(user:User):Output<Int>
    //
    suspend fun insertSession(session: Session): Output<Int>
    suspend fun getSessionsForUser(user: User):Output<List<Session>>
    //
    suspend fun setWorkPeriodLength(durationSeconds:Int)
    suspend fun getWorkPeriodLengthSeconds():Int
    //
    suspend fun setRestPeriodLength(durationSeconds:Int)
    suspend fun getRestPeriodLengthSeconds():Int
    //
    suspend fun setNumberOfWorkPeriods(number:Int)
    suspend fun getNumberOfWorkPeriods():Int
    //
    suspend fun setBeepSound(active:Boolean)
    suspend fun getBeepSound():Boolean
    //
    suspend fun setSessionStartCountdown(durationSeconds:Int)
    suspend fun getSessionStartCountdown():Int
    //
    suspend fun setPeriodStartCountdown(durationSeconds:Int)
    suspend fun getPeriodStartCountdown():Int
    //
    suspend fun setNumberOfCumulatedCycles(number:Int)
    suspend fun getNumberOfCumulatedCycles():Int
    //
    suspend fun setExercisesTypesSelected(exercisesTypes:List<ExerciseType>)
    suspend fun getExercisesTypesSelected():List<ExerciseType>

}