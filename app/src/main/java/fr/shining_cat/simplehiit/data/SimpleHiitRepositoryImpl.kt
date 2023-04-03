package fr.shining_cat.simplehiit.data

import fr.shining_cat.simplehiit.data.local.database.dao.SessionRecordsDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager
import fr.shining_cat.simplehiit.data.mappers.SessionMapper
import fr.shining_cat.simplehiit.data.mappers.UserMapper
import fr.shining_cat.simplehiit.di.IoDispatcher
import fr.shining_cat.simplehiit.domain.Constants.Errors
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.domain.models.SessionRecord
import fr.shining_cat.simplehiit.domain.models.SimpleHiitPreferences
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class SimpleHiitRepositoryImpl @Inject constructor(
    private val usersDao: UsersDao,
    private val sessionRecordsDao: SessionRecordsDao,
    private val userMapper: UserMapper,
    private val sessionMapper: SessionMapper,
    private val hiitDataStoreManager: SimpleHiitDataStoreManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val hiitLogger: HiitLogger
) : SimpleHiitRepository {

    override suspend fun insertUser(user: User): Output<Long> {
        return withContext(ioDispatcher){
            try {
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
        return  withContext(ioDispatcher){
            try {
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
        return withContext(ioDispatcher){
            try {
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
    }

    /**
     * this will trigger deletion of sessions linked to the user thanks to the foreign-key and the cascade delete
     */
    override suspend fun deleteUser(user: User): Output<Int> {
        return withContext(ioDispatcher){
            try {
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
    }

    override suspend fun deleteAllUsers() {
        withContext(ioDispatcher){
            try {
                usersDao.deleteAllUsers()
            } catch (exception: Exception) {
                hiitLogger.e("SimpleHiitRepositoryImpl", "failed deleting All Users", exception)
                throw exception
            }
        }
    }

    override suspend fun insertSessionRecord(sessionRecord: SessionRecord): Output<Int> {
        if (sessionRecord.usersIds.isEmpty()) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "insertSession::Error - no user provided")
            return Output.Error(
                errorCode = Errors.NO_USER_PROVIDED,
                exception = Exception("No user provided when trying to insert session")
            )
        }
        return withContext(ioDispatcher){
            try {
                val sessionEntities = sessionMapper.convert(sessionRecord)
                val insertedIds = sessionRecordsDao.insert(sessionEntities)
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
    }

    override suspend fun getSessionRecordsForUser(user: User): Output<List<SessionRecord>> {
        return withContext(ioDispatcher){
            try {
                val sessions = sessionRecordsDao.getSessionsForUser(user.id)
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
    }

    override suspend fun deleteSessionRecordsForUser(userId: Long): Output<Int> {
        return withContext(ioDispatcher){
            try {
                val deletedCount = sessionRecordsDao.deleteForUser(userId)
                Output.Success(result = deletedCount)
            } catch (cancellationException: CancellationException) {
                throw cancellationException //filter out this exception to avoid blocking the natural handling of cancellation by the coroutine flow
            } catch (exception: Exception) {
                hiitLogger.e("SimpleHiitRepositoryImpl", "failed deleting sessions for user", exception)
                Output.Error(errorCode = Errors.DATABASE_DELETE_FAILED, exception = exception)
            }
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
        withContext(ioDispatcher){
            hiitDataStoreManager.setWorkPeriodLength(durationMs)
        }
    }

    override suspend fun setRestPeriodLength(durationMs: Long) {
        withContext(ioDispatcher){
            hiitDataStoreManager.setRestPeriodLength(durationMs)
        }
    }

    override suspend fun setNumberOfWorkPeriods(number: Int) {
        withContext(ioDispatcher) {
            hiitDataStoreManager.setNumberOfWorkPeriods(number)
        }
    }

    override suspend fun setBeepSound(active: Boolean) {
        withContext(ioDispatcher){
            hiitDataStoreManager.setBeepSound(active)
        }
    }

    override suspend fun setSessionStartCountdown(durationMs: Long) {
        withContext(ioDispatcher){
            hiitDataStoreManager.setSessionStartCountdown(durationMs)
        }
    }

    override suspend fun setPeriodStartCountdown(durationMs: Long) {
        withContext(ioDispatcher) {
            hiitDataStoreManager.setPeriodStartCountdown(durationMs)
        }
    }

    override suspend fun setTotalRepetitionsNumber(number: Int) {
        withContext(ioDispatcher){
            hiitDataStoreManager.setNumberOfCumulatedCycles(number = number)
        }
    }

    override suspend fun setExercisesTypesSelected(exercisesTypes: List<ExerciseType>) {
        withContext(ioDispatcher){
            hiitDataStoreManager.setExercisesTypesSelected(exercisesTypes = exercisesTypes)
        }
    }

    override suspend fun resetAllSettings() {
        withContext(ioDispatcher){
            hiitDataStoreManager.clearAll()
        }
    }
}
