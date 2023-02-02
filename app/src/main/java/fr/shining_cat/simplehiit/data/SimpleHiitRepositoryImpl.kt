package fr.shining_cat.simplehiit.data

import fr.shining_cat.simplehiit.utils.HiitLogger
import fr.shining_cat.simplehiit.data.local.database.dao.SessionsDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences
import fr.shining_cat.simplehiit.data.mappers.SessionMapper
import fr.shining_cat.simplehiit.data.mappers.UserMapper
import fr.shining_cat.simplehiit.domain.Constants.Errors
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.domain.models.Session
import fr.shining_cat.simplehiit.domain.models.User
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class SimpleHiitRepositoryImpl @Inject constructor(
    private val usersDao: UsersDao,
    private val sessionsDao: SessionsDao,
    private val userMapper: UserMapper,
    private val sessionMapper: SessionMapper,
    private val hiitPreferences: SimpleHiitPreferences,
    private val hiitLogger: HiitLogger
) : SimpleHiitRepository {

    override suspend fun insertUser(user: User): Output<Long> {
        return try {
            val insertedId = usersDao.insert(userMapper.convert(user))
            Output.Success(result = insertedId)
        } catch (cancellationException: CancellationException) {
            throw cancellationException //filter out this exception to avoid blocking the natural handling of cancellation by the coroutine flow
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "failed inserting user", exception)
            Output.Error(
                errorCode = Errors.DATABASE_INSERT_FAILED, exception = exception
            )
        }
    }

    override suspend fun getUsers(): Output<List<User>> {
        return try {
            val users = usersDao.getUsers()
            val usersModels = users.map { userMapper.convert(it) }
            Output.Success(usersModels)
        } catch (cancellationException: CancellationException) {
            throw cancellationException //filter out this exception to avoid blocking the natural handling of cancellation by the coroutine flow
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "failed getting users", exception)
            Output.Error(
                errorCode = Errors.DATABASE_FETCH_FAILED, exception = exception
            )
        }
    }

    override suspend fun getSelectedUsers(): Output<List<User>> {
        return try {
            val users = usersDao.getSelectedUsers()
            val usersModels = users.map { userMapper.convert(it) }
            Output.Success(usersModels)
        } catch (cancellationException: CancellationException) {
            throw cancellationException //filter out this exception to avoid blocking the natural handling of cancellation by the coroutine flow
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "failed getting selected users", exception)
            Output.Error(
                errorCode = Errors.DATABASE_FETCH_FAILED, exception = exception
            )
        }
    }

    override suspend fun updateUser(user: User): Output<Int> {
        return try {
            val numberOfUpdates = usersDao.update(userMapper.convert(user))
            if (numberOfUpdates == 1) {
                Output.Success(result = numberOfUpdates)
            } else {
                hiitLogger.e("SimpleHiitRepositoryImpl", "failed updating user")
                Output.Error(
                    errorCode = Errors.DATABASE_UPDATE_FAILED,
                    exception = Exception("failed updating user")
                )
            }
        } catch (cancellationException: CancellationException) {
            throw cancellationException //filter out this exception to avoid blocking the natural handling of cancellation by the coroutine flow
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "failed updating user", exception)
            Output.Error(
                errorCode = Errors.DATABASE_UPDATE_FAILED, exception = exception
            )
        }
    }

    /**
     * this will trigger deletion of sessions linked to the user thanks to the foreign-key and the cascade delete
     */
    override suspend fun deleteUser(user: User): Output<Int> {
        return try {
            val deletedCount = usersDao.delete(userMapper.convert(user))
            if(deletedCount == 1){
                Output.Success(result = deletedCount)
            } else{
                hiitLogger.e("SimpleHiitRepositoryImpl", "failed deleting user")
                Output.Error(
                    errorCode = Errors.DATABASE_DELETE_FAILED,
                    exception = Exception("failed deleting user")
                )
            }
        } catch (cancellationException: CancellationException) {
            throw cancellationException //filter out this exception to avoid blocking the natural handling of cancellation by the coroutine flow
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "failed deleting user", exception)
            Output.Error(errorCode = Errors.DATABASE_DELETE_FAILED, exception = exception)
        }
    }

    override suspend fun insertSession(session: Session): Output<Int> {
        if (session.usersIds.isEmpty()) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "insertSession::Error - no user provided")
            return Output.Error(
                errorCode = Errors.NO_USER_PROVIDED,
                exception = Exception("No user provided when trying to insert session")
            )
        }
        return try {
            val sessionEntities = sessionMapper.convert(session)
            val insertedIds = sessionsDao.insert(sessionEntities)
            Output.Success(insertedIds.size)
        } catch (cancellationException: CancellationException) {
            throw cancellationException //filter out this exception to avoid blocking the natural handling of cancellation by the coroutine flow
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "failed inserting session", exception)
            Output.Error(
                errorCode = Errors.DATABASE_INSERT_FAILED, exception = exception
            )
        }
    }

    override suspend fun getSessionsForUser(user: User): Output<List<Session>> {
        return try {
            val sessions = sessionsDao.getSessionsForUser(user.id)
            hiitLogger.d(
                "SimpleHiitRepositoryImpl",
                "getSessionsForUser::found ${sessions.size} sessions"
            )
            val sessionsModels = sessions.map { sessionMapper.convert(it) }
            Output.Success(sessionsModels)
        } catch (cancellationException: CancellationException) {
            throw cancellationException //filter out this exception to avoid blocking the natural handling of cancellation by the coroutine flow
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "failed getting sessions", exception)
            Output.Error(
                errorCode = Errors.DATABASE_FETCH_FAILED, exception = exception
            )
        }
    }

    override suspend fun setWorkPeriodLength(durationSeconds: Int) {
        hiitPreferences.setWorkPeriodLength(durationSeconds)
    }

    override suspend fun getWorkPeriodLengthSeconds(): Int {
        return hiitPreferences.getWorkPeriodLengthSeconds()
    }

    override suspend fun setRestPeriodLength(durationSeconds: Int) {
        hiitPreferences.setRestPeriodLength(durationSeconds)
    }

    override suspend fun getRestPeriodLengthSeconds(): Int {
        return hiitPreferences.getRestPeriodLengthSeconds()
    }

    override suspend fun setNumberOfWorkPeriods(number: Int) {
        hiitPreferences.setNumberOfWorkPeriods(number)
    }

    override suspend fun getNumberOfWorkPeriods(): Int {
        return hiitPreferences.getNumberOfWorkPeriods()
    }

    override suspend fun setBeepSound(active: Boolean) {
        hiitPreferences.setBeepSound(active)
    }

    override suspend fun getBeepSound(): Boolean {
        return hiitPreferences.getBeepSoundActive()
    }

    override suspend fun setSessionStartCountdown(durationSeconds: Int) {
        hiitPreferences.setSessionStartCountdown(durationSeconds)
    }

    override suspend fun getSessionStartCountdown(): Int {
        return hiitPreferences.getSessionStartCountdown()
    }

    override suspend fun setPeriodStartCountdown(durationSeconds: Int) {
        hiitPreferences.setPeriodStartCountdown(durationSeconds)
    }

    override suspend fun getPeriodStartCountdown(): Int {
        return hiitPreferences.getPeriodStartCountdown()
    }

    override suspend fun setNumberOfCumulatedCycles(number: Int) {
        hiitPreferences.setNumberOfCumulatedCycles(number = number)
    }

    override suspend fun getNumberOfCumulatedCycles(): Int {
        return hiitPreferences.getNumberOfCumulatedCycles()
    }

    override suspend fun setExercisesTypesSelected(exercisesTypes:List<ExerciseType>){
        hiitPreferences.setExercisesTypesSelected(exercisesTypes = exercisesTypes)
    }
    override suspend fun getExercisesTypesSelected():List<ExerciseType>{
        return hiitPreferences.getExercisesTypesSelected()
    }

}
