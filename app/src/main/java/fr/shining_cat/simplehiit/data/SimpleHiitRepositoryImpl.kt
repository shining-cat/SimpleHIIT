package fr.shining_cat.simplehiit.data

import fr.shining_cat.simplehiit.utils.HiitLogger
import fr.shining_cat.simplehiit.data.local.database.dao.SessionsDao
import fr.shining_cat.simplehiit.data.local.database.dao.SessionsUsersLinkDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.database.entities.SessionsUsersLinkEntity
import fr.shining_cat.simplehiit.data.mappers.SessionMapper
import fr.shining_cat.simplehiit.data.mappers.UserMapper
import fr.shining_cat.simplehiit.domain.Constants.Errors
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.Session
import fr.shining_cat.simplehiit.domain.models.User
import javax.inject.Inject

class SimpleHiitRepositoryImpl @Inject constructor(
    private val usersDao: UsersDao,
    private val sessionsDao: SessionsDao,
    private val sessionsUsersLinkDao: SessionsUsersLinkDao,
    private val userMapper:UserMapper,
    private val sessionMapper: SessionMapper,
    private val hiitLogger: HiitLogger
) : SimpleHiitRepository {

    override suspend fun insertUser(user: User): Output<Long> {
        return try {
            val insertedId = usersDao.insert(userMapper.convert(user))
            Output.Success(result = insertedId)
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "insertUser::Error", exception)
            Output.Error(
                errorCode = Errors.DATABASE_INSERT_FAILED, exception = exception
            )
        }
    }

    override suspend fun getUsers(): Output<List<User>> {
        return try {
            val users = usersDao.getUsers()
            val usersModels = users.map { userMapper.convert(it)}
            Output.Success(usersModels)
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "getUsers::Error", exception)
            Output.Error(
                errorCode = Errors.DATABASE_FETCH_FAILED, exception = exception
            )
        }
    }

    override suspend fun updateUser(user: User): Output<Int> {
        return try {
            val numberOfUpdates = usersDao.update(userMapper.convert(user))
            if(numberOfUpdates == 1) {
                Output.Success(result = numberOfUpdates)
            } else{
                hiitLogger.e("SimpleHiitRepositoryImpl", "updateUser::Error")
                Output.Error(
                    errorCode = Errors.DATABASE_UPDATE_FAILED, exception = Exception("failed updating user")
                )
            }
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "insertUser::Error", exception)
            Output.Error(
                errorCode = Errors.DATABASE_UPDATE_FAILED, exception = exception
            )
        }
    }

    override suspend fun deleteUser(user: User): Output<Int> {
        return try {
            //first find all user-sessions links tied to the user
            val linksForUser = sessionsUsersLinkDao.getSessionsUsersLinksForUser(user.id)
            for(link in linksForUser) {
                //find all eventual others sessions links tied to the same session
                val linksForSession = sessionsUsersLinkDao.getSessionsUsersLinksForSession(link.sessionId)
                if (linksForSession.size == 1) {
                    if (linksForSession[0].userId == user.id){
                        hiitLogger.d(
                            "SimpleHiitRepositoryImpl",
                            "deleteUser::session only linked to targeted user, deleting link AND session"
                        )
                        //this session is only linked to the targeted user => delete link then session
                        val deleteNumber = sessionsUsersLinkDao.deleteByLinkId(link.linkId)
                        if(deleteNumber != 1){
                            hiitLogger.e("SimpleHiitRepositoryImpl", "deleteUser::Error::link deletion failed")
                        }
                        sessionsDao.delete(link.sessionId)
                    } else {
                        //This should really never happen, the data is obviously corrupted: the session found is linked to an other user
                        hiitLogger.e(
                            "SimpleHiitRepositoryImpl",
                            "deleteUser::Error::Inconsistent data -> deleting ONLY found link for input user"
                        )
                        val deleteNumber = sessionsUsersLinkDao.deleteByLinkId(link.linkId)
                        if (deleteNumber != 1) {
                            hiitLogger.e("SimpleHiitRepositoryImpl","deleteUser::Error::link deletion failed")
                        }
                    }
                }else{
                    hiitLogger.d("SimpleHiitRepositoryImpl", "deleteUser::session linked to other user(s), ONLY deleting link")
                    //this session is linked to other user(s) => only delete the link
                    sessionsUsersLinkDao.deleteByLinkId(link.linkId)
                }
            }
            //finally we can delete the user
            hiitLogger.d("SimpleHiitRepositoryImpl", "deleteUser::deleting user")
            val numberOfDeletes = usersDao.delete(userMapper.convert(user))
            if(numberOfDeletes == 1) {
                Output.Success(result = numberOfDeletes)
            } else{
                hiitLogger.e("SimpleHiitRepositoryImpl", "deleteUser::Error")
                Output.Error(
                    errorCode = Errors.DATABASE_DELETE_FAILED, exception = Exception("failed updating user")
                )
            }
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "deleteUser::Error", exception)
            Output.Error(
                errorCode = Errors.DATABASE_DELETE_FAILED, exception = exception
            )
        }
    }

    override suspend fun insertSession(session: Session): Output<Long> {
        if(session.users.isEmpty()) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "insertSession::Error - no user provided")
            return Output.Error(
                errorCode = Errors.DATABASE_FETCH_FAILED, exception = Exception("No user provided when trying to insert session")
            )
        }
        return try {
            val sessionEntity = sessionMapper.convert(session)
            val insertedSessionId = sessionsDao.insert(sessionEntity)
            val sessionsUsersLinks = session.users.map{user ->
                SessionsUsersLinkEntity(sessionId = insertedSessionId, userId = user.id)
            }
            val insertedSessionsUsersLinks = sessionsUsersLinkDao.insert(sessionsUsersLinks = sessionsUsersLinks)
            if(insertedSessionsUsersLinks.size == session.users.size){
                Output.Success(insertedSessionId)
            } else{
                hiitLogger.e("SimpleHiitRepositoryImpl", "insertSession::Error - deleting inserted session from sessionsDao")
                sessionsDao.delete(insertedSessionId)
                for(insertedLinkId in insertedSessionsUsersLinks){
                    hiitLogger.e("SimpleHiitRepositoryImpl", "insertSession::Error - deleting inserted session-user link from sessionsUsersLinkDao")
                    sessionsUsersLinkDao.deleteByLinkId(insertedLinkId)
                }
                Output.Error(
                    errorCode = Errors.DATABASE_INSERT_FAILED, exception = Exception("failed inserting users to sessions links, deleted partially inserted data from DB")
                )
            }
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "insertSession::Error", exception)
            Output.Error(
                errorCode = Errors.DATABASE_INSERT_FAILED, exception = exception
            )
        }
    }

    override suspend fun getSessionsForUser(user: User): Output<List<Session>> {
        return try {
            val links = sessionsUsersLinkDao.getSessionsUsersLinksForUser(user.id)
            hiitLogger.d("SimpleHiitRepositoryImpl", "getSessionsForUser::found ${links.size} session-user links")
            val sessionsIds = links.map { it.sessionId }
            val sessions = sessionsDao.getSessions(sessionsIds)
            hiitLogger.d("SimpleHiitRepositoryImpl", "getSessionsForUser::found ${sessions.size} sessions")
            val sessionsModels = sessions.map { sessionMapper.convert(it)}
            Output.Success(sessionsModels)
        } catch (exception: Exception) {
            hiitLogger.e("SimpleHiitRepositoryImpl", "getSessionsForUser::Error", exception)
            Output.Error(
                errorCode = Errors.DATABASE_FETCH_FAILED, exception = exception
            )
        }
    }

}
