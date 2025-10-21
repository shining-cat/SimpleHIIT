package fr.shiningcat.simplehiit.data.repositories

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.data.di.IoDispatcher
import fr.shiningcat.simplehiit.data.local.database.dao.UsersDao
import fr.shiningcat.simplehiit.data.mappers.UserMapper
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.UsersRepository
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRepositoryImpl
    @Inject
    constructor(
        private val usersDao: UsersDao,
        private val userMapper: UserMapper,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
        private val hiitLogger: HiitLogger,
    ) : UsersRepository {
        override suspend fun insertUser(user: User): Output<Long> =
            withContext(ioDispatcher) {
                try {
                    val insertedId = usersDao.insert(userMapper.convert(user))
                    Output.Success(result = insertedId)
                } catch (exception: Exception) {
                    this.coroutineContext.ensureActive()
                    hiitLogger.e("UsersRepositoryImpl", "failed inserting user", exception)
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_INSERT_FAILED,
                        exception = exception,
                    )
                }
            }

        override fun getUsers(): Flow<Output<List<User>>> =
            try {
                val usersFlow = usersDao.getUsers()
                usersFlow.map { users ->
                    Output.Success(
                        users.map { user ->
                            userMapper.convert(user)
                        },
                    )
                }
            } catch (exception: Exception) {
                hiitLogger.e("UsersRepositoryImpl", "failed getting users", exception)
                flowOf(
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_FETCH_FAILED,
                        exception = exception,
                    ),
                )
            }

        override suspend fun getUsersList(): Output<List<User>> =
            withContext(ioDispatcher) {
                try {
                    Output.Success(
                        result =
                            usersDao.getUsersList().map { user ->
                                userMapper.convert(user)
                            },
                    )
                } catch (exception: Exception) {
                    this.coroutineContext.ensureActive()
                    hiitLogger.e("UsersRepositoryImpl", "failed getting users as List", exception)
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_FETCH_FAILED,
                        exception = exception,
                    )
                }
            }

        override fun getSelectedUsers(): Flow<Output<List<User>>> =
            try {
                val usersFlow = usersDao.getSelectedUsers()
                usersFlow.map { users ->
                    Output.Success(
                        users.map { user ->
                            userMapper.convert(user)
                        },
                    )
                }
            } catch (exception: Exception) {
                hiitLogger.e("UsersRepositoryImpl", "failed getting selected users", exception)
                flowOf(
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_FETCH_FAILED,
                        exception = exception,
                    ),
                )
            }

        override suspend fun updateUser(user: User): Output<Int> =
            withContext(ioDispatcher) {
                try {
                    val numberOfUpdates = usersDao.update(userMapper.convert(user))
                    if (numberOfUpdates == 1) {
                        Output.Success(result = numberOfUpdates)
                    } else {
                        hiitLogger.e("UsersRepositoryImpl", "failed updating user")
                        Output.Error(
                            errorCode = Constants.Errors.DATABASE_UPDATE_FAILED,
                            exception = Exception("failed updating user"),
                        )
                    }
                } catch (exception: Exception) {
                    this.coroutineContext.ensureActive()
                    hiitLogger.e("UsersRepositoryImpl", "failed updating user", exception)
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_UPDATE_FAILED,
                        exception = exception,
                    )
                }
            }

        /**
         * this will trigger deletion of sessions linked to the user thanks to the foreign-key and the cascade delete
         */
        override suspend fun deleteUser(user: User): Output<Int> =
            withContext(ioDispatcher) {
                try {
                    val deletedCount = usersDao.delete(userMapper.convert(user))
                    if (deletedCount == 1) {
                        Output.Success(result = deletedCount)
                    } else {
                        hiitLogger.e("UsersRepositoryImpl", "failed deleting user")
                        Output.Error(
                            errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
                            exception = Exception("failed deleting user"),
                        )
                    }
                } catch (exception: Exception) {
                    this.coroutineContext.ensureActive()
                    hiitLogger.e("UsersRepositoryImpl", "failed deleting user", exception)
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
                        exception = exception,
                    )
                }
            }

        override suspend fun deleteAllUsers() {
            withContext(ioDispatcher) {
                try {
                    usersDao.deleteAllUsers()
                } catch (exception: Exception) {
                    // we never wait for any result from here, so we can simply rethrow any eventual exception
                    hiitLogger.e("UsersRepositoryImpl", "failed deleting All Users", exception)
                    throw exception
                }
            }
        }
    }
