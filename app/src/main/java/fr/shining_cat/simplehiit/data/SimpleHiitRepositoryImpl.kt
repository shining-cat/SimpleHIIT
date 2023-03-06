package fr.shining_cat.simplehiit.data

import fr.shining_cat.simplehiit.data.local.database.dao.SessionsDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager
import fr.shining_cat.simplehiit.data.mappers.SessionMapper
import fr.shining_cat.simplehiit.data.mappers.UserMapper
import fr.shining_cat.simplehiit.domain.Constants.Errors
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.domain.models.Session
import fr.shining_cat.simplehiit.domain.models.SimpleHiitPreferences
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class SimpleHiitRepositoryImpl @Inject constructor(
    private val usersDao: UsersDao,
    private val sessionsDao: SessionsDao,
    private val userMapper: UserMapper,
    private val sessionMapper: SessionMapper,
    private val hiitDataStoreManager: SimpleHiitDataStoreManager,
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

    override fun getUsers(): Flow<Output<List<User>>> {
        return try {
            val usersFlow = usersDao.getUsers()
            usersFlow.map { users ->
                Output.Success(
                    users.map { user ->
                        userMapper.convert(user)
                    }
                )
            }
        } catch (cancellationException: CancellationException) {
            throw cancellationException //filter out this exception to avoid blocking the natural handling of cancellation by the coroutine flow
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "failed getting users", exception)
            flowOf(
                Output.Error(errorCode = Errors.DATABASE_FETCH_FAILED, exception = exception)
            )
        }
    }

    override suspend fun getUsersList(): Output<List<User>> {
        return try {
            Output.Success(result = usersDao.getUsersList().map { user ->
                userMapper.convert(user)
            })
        } catch (cancellationException: CancellationException) {
            throw cancellationException //filter out this exception to avoid blocking the natural handling of cancellation by the coroutine flow
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "failed getting users as List", exception)
            Output.Error(errorCode = Errors.DATABASE_FETCH_FAILED, exception = exception)
        }
    }

    override fun getSelectedUsers(): Flow<Output<List<User>>> {
        return try {
            val usersFlow = usersDao.getSelectedUsers()
            usersFlow.map { users ->
                Output.Success(
                    users.map { user ->
                        userMapper.convert(user)
                    }
                )
            }
        } catch (cancellationException: CancellationException) {
            throw cancellationException //filter out this exception to avoid blocking the natural handling of cancellation by the coroutine flow
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "failed getting selected users", exception)
            flowOf(
                Output.Error(errorCode = Errors.DATABASE_FETCH_FAILED, exception = exception)
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
            if (deletedCount == 1) {
                Output.Success(result = deletedCount)
            } else {
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

    override suspend fun deleteAllUsers() {
        try {
            usersDao.deleteAllUsers()
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "failed deleting All Users", exception)
            throw exception
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

    override suspend fun deleteSessionsForUser(userId: Long): Output<Int> {
        return try {
            val deletedCount = sessionsDao.deleteForUser(userId)
            Output.Success(result = deletedCount)
        } catch (cancellationException: CancellationException) {
            throw cancellationException //filter out this exception to avoid blocking the natural handling of cancellation by the coroutine flow
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "failed deleting sessions for user", exception)
            Output.Error(errorCode = Errors.DATABASE_DELETE_FAILED, exception = exception)
        }
    }

    override fun getPreferences(): Flow<SimpleHiitPreferences> {
        return try {
            hiitDataStoreManager.getPreferences()
        } catch (cancellationException: CancellationException) {
            throw cancellationException //filter out this exception to avoid blocking the natural handling of cancellation by the coroutine flow
        } catch (exception: Exception) {
            hiitLogger.e(
                "SimpleHiitRepositoryImpl",
                "failed getting general settings - returning default settings",
                exception
            )
            flowOf(SimpleHiitPreferences())
        }
    }

    override suspend fun setWorkPeriodLength(durationMs: Long) {
        hiitDataStoreManager.setWorkPeriodLength(durationMs)
    }

    override suspend fun setRestPeriodLength(durationMs: Long) {
        hiitDataStoreManager.setRestPeriodLength(durationMs)
    }

    override suspend fun setNumberOfWorkPeriods(number: Int) {
        hiitDataStoreManager.setNumberOfWorkPeriods(number)
    }

    override suspend fun setBeepSound(active: Boolean) {
        hiitDataStoreManager.setBeepSound(active)
    }

    override suspend fun setSessionStartCountdown(durationMs: Long) {
        hiitDataStoreManager.setSessionStartCountdown(durationMs)
    }

    override suspend fun setPeriodStartCountdown(durationMs: Long) {
        hiitDataStoreManager.setPeriodStartCountdown(durationMs)
    }

    override suspend fun setTotalRepetitionsNumber(number: Int) {
        hiitDataStoreManager.setNumberOfCumulatedCycles(number = number)
    }

    override suspend fun setExercisesTypesSelected(exercisesTypes: List<ExerciseType>) {
        hiitDataStoreManager.setExercisesTypesSelected(exercisesTypes = exercisesTypes)
    }

    override suspend fun resetAllSettings() {
        hiitDataStoreManager.clearAll()
    }
}
