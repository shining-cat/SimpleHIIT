/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.data.repositories

import fr.shiningcat.simplehiit.data.local.database.dao.UsersDao
import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity
import fr.shiningcat.simplehiit.data.mappers.UserMapper
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
internal class UsersRepositoryImplUpdateUserTest : AbstractMockkTest() {
    private val mockUsersDao = mockk<UsersDao>()
    private val mockUserMapper = mockk<UserMapper>()

    private val testUserId = 123L
    private val testUserName = "test user name"
    private val testIsSelected = true
    private val testUserModel =
        User(id = testUserId, name = testUserName, selected = testIsSelected)
    private val testUserEntity =
        UserEntity(userId = testUserId, name = testUserName, selected = testIsSelected)

    // ////////////
//   UPDATE USER
    @Test
    fun `update user returns success output when dao returns 1`() =
        runTest {
            val usersRepository =
                UsersRepositoryImpl(
                    usersDao = mockUsersDao,
                    userMapper = mockUserMapper,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
            coEvery { mockUsersDao.update(any()) } returns 1
            //
            val actual = usersRepository.updateUser(testUserModel)
            //
            coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
            coVerify(exactly = 1) { mockUsersDao.update(testUserEntity) }
            assertEquals(Output.Success(1), actual)
        }

    @Test
    fun `update user throws CancellationException when job is cancelled`() =
        runTest {
            val usersRepository =
                UsersRepositoryImpl(
                    usersDao = mockUsersDao,
                    userMapper = mockUserMapper,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
            coEvery { mockUsersDao.update(any()) } coAnswers {
                println("inserting delay in DAO call to allow for job cancellation before result is returned")
                delay(100L)
                1
            }
            //
            val job = Job()
            launch(job) {
                assertThrows<CancellationException> {
                    usersRepository.updateUser(testUserModel)
                }
            }
            delay(50L)
            println("canceling job")
            job.cancelAndJoin()
            //
            coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
            coVerify(exactly = 1) { mockUsersDao.update(testUserEntity) }
            coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
        }

    @ParameterizedTest(name = "{index} -> when DAO update user returns {0} should return error")
    @MethodSource("updateUserArguments")
    fun `update user returns correct output`(
        daoAnswer: Int,
        expectedOutput: Output.Error,
    ) = runTest {
        val usersRepository =
            UsersRepositoryImpl(
                usersDao = mockUsersDao,
                userMapper = mockUserMapper,
                ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                hiitLogger = mockHiitLogger,
            )
        //
        coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
        coEvery { mockUsersDao.update(any()) } answers { daoAnswer }
        //
        val actual = usersRepository.updateUser(testUserModel)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
        coVerify(exactly = 1) { mockUsersDao.update(testUserEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "failed updating user") }
        assertTrue(actual is Output.Error)
        actual as Output.Error
        assertEquals(expectedOutput.errorCode, actual.errorCode)
        assertEquals(expectedOutput.exception.message, actual.exception.message)
    }

    @Test
    fun `update user returns error when dao update users throws exception`() =
        runTest {
            val usersRepository =
                UsersRepositoryImpl(
                    usersDao = mockUsersDao,
                    userMapper = mockUserMapper,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
            val thrownException = Exception("this is a test exception")
            coEvery { mockUsersDao.update(any()) } throws thrownException
            //
            val actual = usersRepository.updateUser(testUserModel)
            //
            coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
            coVerify(exactly = 1) { mockUsersDao.update(testUserEntity) }
            coVerify(exactly = 1) {
                mockHiitLogger.e(
                    any(),
                    "failed updating user",
                    thrownException,
                )
            }
            val expectedOutput =
                Output.Error(
                    errorCode = DomainError.DATABASE_UPDATE_FAILED,
                    exception = thrownException,
                )
            assertEquals(expectedOutput, actual)
        }

    @Test
    fun `update rethrows catches rogue CancellationException`() =
        runTest {
            val usersRepository =
                UsersRepositoryImpl(
                    usersDao = mockUsersDao,
                    userMapper = mockUserMapper,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
            val thrownException = CancellationException()
            coEvery { mockUsersDao.update(any()) } throws thrownException
            //
            val actual = usersRepository.updateUser(testUserModel)
            //
            coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
            coVerify(exactly = 1) { mockUsersDao.update(testUserEntity) }
            coVerify(exactly = 1) {
                mockHiitLogger.e(
                    any(),
                    "failed updating user",
                    thrownException,
                )
            }
            val expectedOutput =
                Output.Error(
                    errorCode = DomainError.DATABASE_UPDATE_FAILED,
                    exception = thrownException,
                )
            assertEquals(expectedOutput, actual)
        }

    // //////////////////////
    private companion object {
        @JvmStatic
        fun updateUserArguments() =
            Stream.of(
                Arguments.of(
                    0,
                    Output.Error(
                        errorCode = DomainError.DATABASE_UPDATE_FAILED,
                        exception = Exception("failed updating user"),
                    ),
                ),
                Arguments.of(
                    7,
                    Output.Error(
                        errorCode = DomainError.DATABASE_UPDATE_FAILED,
                        exception = Exception("failed updating user"),
                    ),
                ),
            )
    }
}
