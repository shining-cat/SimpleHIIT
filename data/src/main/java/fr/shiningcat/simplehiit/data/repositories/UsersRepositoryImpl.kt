/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.data.repositories

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.data.local.database.dao.UsersDao
import fr.shiningcat.simplehiit.data.mappers.UserMapper
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.UsersRepository
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UsersRepositoryImpl(
    private val usersDao: UsersDao,
    private val userMapper: UserMapper,
    private val ioDispatcher: CoroutineDispatcher,
    private val hiitLogger: HiitLogger,
) : UsersRepository {
    override suspend fun insertUser(user: User): Output<Long> =
        withContext(ioDispatcher) {
            runCatching {
                usersDao.insert(userMapper.convert(user))
            }.fold(
                onSuccess = { insertedId -> Output.Success(result = insertedId) },
                onFailure = { exception ->
                    coroutineContext.ensureActive()
                    hiitLogger.e("UsersRepositoryImpl", "failed inserting user", exception)
                    Output.Error(
                        errorCode = DomainError.DATABASE_INSERT_FAILED,
                        exception = exception,
                    )
                },
            )
        }

    override fun getUsers(): Flow<Output<List<User>>> =
        runCatching {
            usersDao.getUsers().map { users ->
                Output.Success(
                    users.map { user ->
                        userMapper.convert(user)
                    },
                )
            }
        }.getOrElse { exception ->
            hiitLogger.e("UsersRepositoryImpl", "failed getting users", exception)
            flowOf(
                Output.Error(
                    errorCode = DomainError.DATABASE_FETCH_FAILED,
                    exception = exception,
                ),
            )
        }

    override suspend fun getUsersList(): Output<List<User>> =
        withContext(ioDispatcher) {
            runCatching {
                usersDao.getUsersList().map { user ->
                    userMapper.convert(user)
                }
            }.fold(
                onSuccess = { result -> Output.Success(result = result) },
                onFailure = { exception ->
                    coroutineContext.ensureActive()
                    hiitLogger.e("UsersRepositoryImpl", "failed getting users as List", exception)
                    Output.Error(
                        errorCode = DomainError.DATABASE_FETCH_FAILED,
                        exception = exception,
                    )
                },
            )
        }

    override fun getSelectedUsers(): Flow<Output<List<User>>> =
        runCatching {
            usersDao.getSelectedUsers().map { users ->
                Output.Success(
                    users.map { user ->
                        userMapper.convert(user)
                    },
                )
            }
        }.getOrElse { exception ->
            hiitLogger.e("UsersRepositoryImpl", "failed getting selected users", exception)
            flowOf(
                Output.Error(
                    errorCode = DomainError.DATABASE_FETCH_FAILED,
                    exception = exception,
                ),
            )
        }

    override suspend fun updateUser(user: User): Output<Int> =
        withContext(ioDispatcher) {
            runCatching {
                usersDao.update(userMapper.convert(user))
            }.fold(
                onSuccess = { numberOfUpdates ->
                    if (numberOfUpdates == 1) {
                        Output.Success(result = numberOfUpdates)
                    } else {
                        hiitLogger.e("UsersRepositoryImpl", "failed updating user")
                        Output.Error(
                            errorCode = DomainError.DATABASE_UPDATE_FAILED,
                            exception = Exception("failed updating user"),
                        )
                    }
                },
                onFailure = { exception ->
                    coroutineContext.ensureActive()
                    hiitLogger.e("UsersRepositoryImpl", "failed updating user", exception)
                    Output.Error(
                        errorCode = DomainError.DATABASE_UPDATE_FAILED,
                        exception = exception,
                    )
                },
            )
        }

    /**
     * this will trigger deletion of sessions linked to the user thanks to the foreign-key and the cascade delete
     */
    override suspend fun deleteUser(user: User): Output<Int> =
        withContext(ioDispatcher) {
            runCatching {
                usersDao.delete(userMapper.convert(user))
            }.fold(
                onSuccess = { deletedCount ->
                    if (deletedCount == 1) {
                        Output.Success(result = deletedCount)
                    } else {
                        hiitLogger.e("UsersRepositoryImpl", "failed deleting user")
                        Output.Error(
                            errorCode = DomainError.DATABASE_DELETE_FAILED,
                            exception = Exception("failed deleting user"),
                        )
                    }
                },
                onFailure = { exception ->
                    coroutineContext.ensureActive()
                    hiitLogger.e("UsersRepositoryImpl", "failed deleting user", exception)
                    Output.Error(
                        errorCode = DomainError.DATABASE_DELETE_FAILED,
                        exception = exception,
                    )
                },
            )
        }

    override suspend fun deleteAllUsers() {
        withContext(ioDispatcher) {
            runCatching {
                usersDao.deleteAllUsers()
            }.onFailure { exception ->
                // we never wait for any result from here, so we can simply rethrow any eventual exception
                hiitLogger.e("UsersRepositoryImpl", "failed deleting All Users", exception)
            }.getOrThrow()
        }
    }
}
