/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.shiningcat.simplehiit.data.local.database.dao.SessionRecordsDao
import fr.shiningcat.simplehiit.data.local.database.dao.UsersDao
import fr.shiningcat.simplehiit.data.local.database.entities.SessionEntity
import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity

@Database(
    entities = [UserEntity::class, SessionEntity::class],
    version = 2,
)
abstract class SimpleHiitDatabase : RoomDatabase() {
    abstract fun userDao(): UsersDao

    abstract fun sessionsDao(): SessionRecordsDao

    companion object {
        val SimpleHiitDatabaseName = "everyday_database.db"
    }
}
