/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity.Companion.USERS_TABLE_NAME

@Entity(tableName = USERS_TABLE_NAME)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = USER_ID_COLUMN_NAME, index = true)
    val userId: Long = 0L,
    @ColumnInfo(name = USER_NAME_COLUMN_NAME) val name: String,
    @ColumnInfo(name = USER_SELECTED_COLUMN_NAME) val selected: Boolean,
    @ColumnInfo(name = USER_LAST_SESSION_TIMESTAMP_COLUMN_NAME) val lastSessionTimestamp: Long? = null,
) {
    companion object {
        const val USERS_TABLE_NAME = "simple_hiit_users"

        //
        const val USER_ID_COLUMN_NAME = "userId"
        const val USER_NAME_COLUMN_NAME = "name"
        const val USER_SELECTED_COLUMN_NAME = "selected"
        const val USER_LAST_SESSION_TIMESTAMP_COLUMN_NAME = "lastSessionTimestamp"
    }
}
