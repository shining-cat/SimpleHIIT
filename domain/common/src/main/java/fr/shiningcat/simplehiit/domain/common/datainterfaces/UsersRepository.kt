/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.common.datainterfaces

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    suspend fun insertUser(user: User): Output<Long>

    fun getUsers(): Flow<Output<List<User>>>

    suspend fun getUsersList(): Output<List<User>>

    fun getSelectedUsers(): Flow<Output<List<User>>>

    suspend fun updateUser(user: User): Output<Int>

    suspend fun deleteUser(user: User): Output<Int>

    suspend fun deleteAllUsers()
}
