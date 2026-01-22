/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.session.usecases

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.UsersRepository
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class UpdateUsersLastSessionTimestampUseCaseTest : AbstractMockkTest() {
    private val mockUsersRepository = mockk<UsersRepository>()

    @Test
    fun `execute with empty user IDs returns success with zero count`() =
        runTest {
            val testedUseCase =
                UpdateUsersLastSessionTimestampUseCase(
                    usersRepository = mockUsersRepository,
                    logger = mockHiitLogger,
                )
            //
            val result = testedUseCase.execute(emptyList(), 123456L)
            //
            coVerify(exactly = 0) { mockUsersRepository.getUsersList() }
            coVerify(exactly = 0) { mockUsersRepository.updateUser(any()) }
            assertTrue(result is Output.Success)
            assertEquals(0, (result as Output.Success).result)
        }

    @Test
    fun `execute successfully updates all users and returns count`() =
        runTest {
            val testedUseCase =
                UpdateUsersLastSessionTimestampUseCase(
                    usersRepository = mockUsersRepository,
                    logger = mockHiitLogger,
                )
            val timestamp = 987654321L
            val user1 = User(id = 1L, name = "User 1", selected = true, lastSessionTimestamp = 0L)
            val user2 = User(id = 2L, name = "User 2", selected = true, lastSessionTimestamp = 0L)
            val user3 = User(id = 3L, name = "User 3", selected = false, lastSessionTimestamp = 0L)
            val allUsers = listOf(user1, user2, user3)

            coEvery { mockUsersRepository.getUsersList() } returns Output.Success(allUsers)
            coEvery { mockUsersRepository.updateUser(any()) } returns Output.Success(1)
            //
            val result = testedUseCase.execute(listOf(1L, 2L), timestamp)
            //
            coVerify(exactly = 1) { mockUsersRepository.getUsersList() }
            coVerify(exactly = 1) { mockUsersRepository.updateUser(user1.copy(lastSessionTimestamp = timestamp)) }
            coVerify(exactly = 1) { mockUsersRepository.updateUser(user2.copy(lastSessionTimestamp = timestamp)) }
            coVerify(exactly = 0) { mockUsersRepository.updateUser(user3.copy(lastSessionTimestamp = timestamp)) }
            assertTrue(result is Output.Success)
            assertEquals(2, (result as Output.Success).result)
        }

    @Test
    fun `execute with non-existent user IDs updates only existing users`() =
        runTest {
            val testedUseCase =
                UpdateUsersLastSessionTimestampUseCase(
                    usersRepository = mockUsersRepository,
                    logger = mockHiitLogger,
                )
            val timestamp = 987654321L
            val user1 = User(id = 1L, name = "User 1", selected = true, lastSessionTimestamp = 0L)
            val allUsers = listOf(user1)

            coEvery { mockUsersRepository.getUsersList() } returns Output.Success(allUsers)
            coEvery { mockUsersRepository.updateUser(any()) } returns Output.Success(1)
            //
            val result = testedUseCase.execute(listOf(1L, 2L, 3L), timestamp)
            //
            coVerify(exactly = 1) { mockUsersRepository.getUsersList() }
            coVerify(exactly = 1) { mockUsersRepository.updateUser(user1.copy(lastSessionTimestamp = timestamp)) }
            assertTrue(result is Output.Success)
            assertEquals(1, (result as Output.Success).result)
        }

    @Test
    fun `execute with update failures counts only successful updates`() =
        runTest {
            val testedUseCase =
                UpdateUsersLastSessionTimestampUseCase(
                    usersRepository = mockUsersRepository,
                    logger = mockHiitLogger,
                )
            val timestamp = 987654321L
            val user1 = User(id = 1L, name = "User 1", selected = true, lastSessionTimestamp = 0L)
            val user2 = User(id = 2L, name = "User 2", selected = true, lastSessionTimestamp = 0L)
            val user3 = User(id = 3L, name = "User 3", selected = false, lastSessionTimestamp = 0L)
            val allUsers = listOf(user1, user2, user3)

            coEvery { mockUsersRepository.getUsersList() } returns Output.Success(allUsers)
            coEvery { mockUsersRepository.updateUser(user1.copy(lastSessionTimestamp = timestamp)) } returns Output.Success(1)
            coEvery { mockUsersRepository.updateUser(user2.copy(lastSessionTimestamp = timestamp)) } returns
                Output.Error(DomainError.DATABASE_UPDATE_FAILED, Exception("Update failed"))
            coEvery { mockUsersRepository.updateUser(user3.copy(lastSessionTimestamp = timestamp)) } returns Output.Success(1)
            //
            val result = testedUseCase.execute(listOf(1L, 2L, 3L), timestamp)
            //
            coVerify(exactly = 1) { mockUsersRepository.getUsersList() }
            coVerify(exactly = 1) { mockUsersRepository.updateUser(user1.copy(lastSessionTimestamp = timestamp)) }
            coVerify(exactly = 1) { mockUsersRepository.updateUser(user2.copy(lastSessionTimestamp = timestamp)) }
            coVerify(exactly = 1) { mockUsersRepository.updateUser(user3.copy(lastSessionTimestamp = timestamp)) }
            assertTrue(result is Output.Success)
            assertEquals(2, (result as Output.Success).result)
        }

    @Test
    fun `execute returns error when getUsersList fails`() =
        runTest {
            val testedUseCase =
                UpdateUsersLastSessionTimestampUseCase(
                    usersRepository = mockUsersRepository,
                    logger = mockHiitLogger,
                )
            val timestamp = 987654321L
            val errorMessage = "Failed to fetch users"
            val expectedError = Output.Error(DomainError.DATABASE_FETCH_FAILED, Exception(errorMessage))

            coEvery { mockUsersRepository.getUsersList() } returns expectedError
            //
            val result = testedUseCase.execute(listOf(1L, 2L), timestamp)
            //
            coVerify(exactly = 1) { mockUsersRepository.getUsersList() }
            coVerify(exactly = 0) { mockUsersRepository.updateUser(any()) }
            assertTrue(result is Output.Error)
            assertEquals(DomainError.DATABASE_FETCH_FAILED, (result as Output.Error).errorCode)
            assertEquals(errorMessage, result.exception?.message)
        }

    @Test
    fun `execute with single user ID updates correctly`() =
        runTest {
            val testedUseCase =
                UpdateUsersLastSessionTimestampUseCase(
                    usersRepository = mockUsersRepository,
                    logger = mockHiitLogger,
                )
            val timestamp = 555555L
            val user = User(id = 42L, name = "Solo User", selected = true, lastSessionTimestamp = 0L)
            val allUsers = listOf(user)

            coEvery { mockUsersRepository.getUsersList() } returns Output.Success(allUsers)
            coEvery { mockUsersRepository.updateUser(any()) } returns Output.Success(1)
            //
            val result = testedUseCase.execute(listOf(42L), timestamp)
            //
            coVerify(exactly = 1) { mockUsersRepository.getUsersList() }
            coVerify(exactly = 1) { mockUsersRepository.updateUser(user.copy(lastSessionTimestamp = timestamp)) }
            assertTrue(result is Output.Success)
            assertEquals(1, (result as Output.Success).result)
        }

    @Test
    fun `execute updates users with existing timestamps correctly`() =
        runTest {
            val testedUseCase =
                UpdateUsersLastSessionTimestampUseCase(
                    usersRepository = mockUsersRepository,
                    logger = mockHiitLogger,
                )
            val oldTimestamp = 111111L
            val newTimestamp = 999999L
            val user1 = User(id = 1L, name = "User 1", selected = true, lastSessionTimestamp = oldTimestamp)
            val user2 = User(id = 2L, name = "User 2", selected = false, lastSessionTimestamp = oldTimestamp)
            val allUsers = listOf(user1, user2)

            coEvery { mockUsersRepository.getUsersList() } returns Output.Success(allUsers)
            coEvery { mockUsersRepository.updateUser(any()) } returns Output.Success(1)
            //
            val result = testedUseCase.execute(listOf(1L, 2L), newTimestamp)
            //
            coVerify(exactly = 1) { mockUsersRepository.getUsersList() }
            coVerify(exactly = 1) { mockUsersRepository.updateUser(user1.copy(lastSessionTimestamp = newTimestamp)) }
            coVerify(exactly = 1) { mockUsersRepository.updateUser(user2.copy(lastSessionTimestamp = newTimestamp)) }
            assertTrue(result is Output.Success)
            assertEquals(2, (result as Output.Success).result)
        }
}
