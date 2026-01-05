package fr.shiningcat.simplehiit.data.repositories

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.data.local.database.dao.SessionRecordsDao
import fr.shiningcat.simplehiit.data.mappers.SessionMapper
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SessionsRepository
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.SessionRecord
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

class SessionsRepositoryImpl(
    private val sessionsDao: SessionRecordsDao,
    private val sessionMapper: SessionMapper,
    private val ioDispatcher: CoroutineDispatcher,
    private val hiitLogger: HiitLogger,
) : SessionsRepository {
    override suspend fun insertSessionRecord(sessionRecord: SessionRecord): Output<Int> {
        if (sessionRecord.usersIds.isEmpty()) {
            hiitLogger.e("SessionsRepositoryImpl", "insertSession::Error - no user provided")
            return Output.Error(
                errorCode = DomainError.NO_USER_PROVIDED,
                exception = Exception("No user provided when trying to insert session"),
            )
        }
        return withContext(ioDispatcher) {
            runCatching {
                val sessionEntities = sessionMapper.convert(sessionRecord)
                sessionsDao.insert(sessionEntities)
            }.fold(
                onSuccess = { insertedIds ->
                    hiitLogger.d(
                        "SessionsRepositoryImpl",
                        "insertSessionRecord::inserted ${insertedIds.size} sessions",
                    )
                    Output.Success(insertedIds.size)
                },
                onFailure = { exception ->
                    coroutineContext.ensureActive()
                    hiitLogger.e("SessionsRepositoryImpl", "failed inserting session", exception)
                    Output.Error(
                        errorCode = DomainError.DATABASE_INSERT_FAILED,
                        exception = exception,
                    )
                },
            )
        }
    }

    override suspend fun getSessionRecordsForUser(user: User): Output<List<SessionRecord>> =
        withContext(ioDispatcher) {
            runCatching {
                sessionsDao.getSessionsForUser(user.id)
            }.fold(
                onSuccess = { sessions ->
                    hiitLogger.d(
                        "SessionsRepositoryImpl",
                        "getSessionsForUser::found ${sessions.size} sessions",
                    )
                    val sessionsModels = sessions.map { sessionMapper.convert(it) }
                    Output.Success(sessionsModels)
                },
                onFailure = { exception ->
                    coroutineContext.ensureActive()
                    hiitLogger.e("SessionsRepositoryImpl", "failed getting sessions", exception)
                    Output.Error(
                        errorCode = DomainError.DATABASE_FETCH_FAILED,
                        exception = exception,
                    )
                },
            )
        }

    override suspend fun deleteSessionRecordsForUser(userId: Long): Output<Int> =
        withContext(ioDispatcher) {
            runCatching {
                sessionsDao.deleteForUser(userId)
            }.fold(
                onSuccess = { deletedCount -> Output.Success(result = deletedCount) },
                onFailure = { exception ->
                    coroutineContext.ensureActive()
                    hiitLogger.e(
                        "SessionsRepositoryImpl",
                        "failed deleting sessions for user",
                        exception,
                    )
                    Output.Error(
                        errorCode = DomainError.DATABASE_DELETE_FAILED,
                        exception = exception,
                    )
                },
            )
        }
}
