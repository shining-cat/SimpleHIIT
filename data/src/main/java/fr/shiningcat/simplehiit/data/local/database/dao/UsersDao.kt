/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity
import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity.Companion.USERS_TABLE_NAME
import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity.Companion.USER_SELECTED_COLUMN_NAME
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(user: UserEntity): Long

    @Query("SELECT * FROM $USERS_TABLE_NAME")
    abstract fun getUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM $USERS_TABLE_NAME")
    abstract suspend fun getUsersList(): List<UserEntity>

    @Query("SELECT * FROM $USERS_TABLE_NAME WHERE $USER_SELECTED_COLUMN_NAME = 1")
    abstract fun getSelectedUsers(): Flow<List<UserEntity>>

    @Update
    abstract suspend fun update(userEntity: UserEntity): Int

    @Delete
    abstract suspend fun delete(userEntity: UserEntity): Int

    @Query("DELETE FROM $USERS_TABLE_NAME")
    abstract suspend fun deleteAllUsers()
}
