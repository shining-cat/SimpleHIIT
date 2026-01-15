/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.data.local.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity

/**
 * Migration from database version 1 to version 2.
 * Adds lastSessionTimestamp column to User table.
 */
val MIGRATION_1_2 =
    object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "ALTER TABLE ${UserEntity.USERS_TABLE_NAME} " +
                    "ADD COLUMN ${UserEntity.USER_LAST_SESSION_TIMESTAMP_COLUMN_NAME} INTEGER",
            )
        }
    }
