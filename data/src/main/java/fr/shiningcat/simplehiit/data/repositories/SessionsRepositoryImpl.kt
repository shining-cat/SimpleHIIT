package fr.shiningcat.simplehiit.data.repositories

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.data.di.IoDispatcher
import fr.shiningcat.simplehiit.data.local.database.dao.SessionRecordsDao
import fr.shiningcat.simplehiit.data.mappers.SessionMapper
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SessionsRepository
import fr.shiningcat.simplehiit.domain.common.models.SessionRecord
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SessionsRepositoryImpl
    @Inject
    constructor(
        private val sessionRecordsDao: SessionRecordsDao,
        private val sessionMapper: SessionMapper,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
        private val hiitLogger: HiitLogger,
    ) : SessionsRepository {
        override suspend fun insertSessionRecord(sessionRecord: SessionRecord): Output<Int> {
            if (sessionRecord.usersIds.isEmpty()) {
                hiitLogger.e("SessionsRepositoryImpl", "insertSession::Error - no user provided")
                return Output.Error(
                    errorCode = Constants.Errors.NO_USER_PROVIDED,
                    exception = Exception("No user provided when trying to insert session"),
                )
            }
            return withContext(ioDispatcher) {
                try {
                    val sessionEntities = sessionMapper.convert(sessionRecord)
                    val insertedIds = sessionRecordsDao.insert(sessionEntities)
                    hiitLogger.d(
                        "SessionsRepositoryImpl",
                        "insertSessionRecord::inserted ${insertedIds.size} sessions",
                    )
                    Output.Success(insertedIds.size)
                } catch (exception: Exception) {
                    this.coroutineContext.ensureActive()
                    hiitLogger.e("SessionsRepositoryImpl", "failed inserting session", exception)
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_INSERT_FAILED,
                        exception = exception,
                    )
                }
            }
        }

        override suspend fun getSessionRecordsForUser(user: User): Output<List<SessionRecord>> =
            withContext(ioDispatcher) {
                try {
                    val sessions = sessionRecordsDao.getSessionsForUser(user.id)
                    hiitLogger.d(
                        "SessionsRepositoryImpl",
                        "getSessionsForUser::found ${sessions.size} sessions",
                    )
                    val sessionsModels = sessions.map { sessionMapper.convert(it) }
                    Output.Success(sessionsModels)
                } catch (exception: Exception) {
                    this.coroutineContext.ensureActive()
                    hiitLogger.e("SessionsRepositoryImpl", "failed getting sessions", exception)
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_FETCH_FAILED,
                        exception = exception,
                    )
                }
            }

        override suspend fun deleteSessionRecordsForUser(userId: Long): Output<Int> =
            withContext(ioDispatcher) {
                try {
                    val deletedCount = sessionRecordsDao.deleteForUser(userId)
                    Output.Success(result = deletedCount)
                } catch (exception: Exception) {
                    this.coroutineContext.ensureActive()
                    hiitLogger.e(
                        "SessionsRepositoryImpl",
                        "failed deleting sessions for user",
                        exception,
                    )
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
                        exception = exception,
                    )
                }
            }
    }
