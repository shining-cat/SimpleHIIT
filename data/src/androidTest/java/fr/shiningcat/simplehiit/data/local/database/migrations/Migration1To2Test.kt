/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.data.local.database.migrations

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.shiningcat.simplehiit.data.local.database.SimpleHiitDatabase
import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class Migration1To2Test {
    private val testDatabaseName = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper =
        MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            SimpleHiitDatabase::class.java,
        )

    @Test
    @Throws(IOException::class)
    fun migrate1To2_addsLastSessionTimestampColumn() {
        // Create database with version 1 schema
        val db = helper.createDatabase(testDatabaseName, 1)

        // Insert test data in version 1
        val testUserId = 1L
        val testUserName = "Test User"
        val testUserSelected = 1 // SQLite uses 1 for true
        db.execSQL(
            "INSERT INTO ${UserEntity.USERS_TABLE_NAME} " +
                "(${UserEntity.USER_ID_COLUMN_NAME}, ${UserEntity.USER_NAME_COLUMN_NAME}, ${UserEntity.USER_SELECTED_COLUMN_NAME}) " +
                "VALUES ($testUserId, '$testUserName', $testUserSelected)",
        )
        db.close()

        // Run migration
        val migratedDb = helper.runMigrationsAndValidate(testDatabaseName, 2, true, MIGRATION_1_2)

        // Verify the new column exists and has NULL value for existing records
        val cursor =
            migratedDb.query("SELECT * FROM ${UserEntity.USERS_TABLE_NAME} WHERE ${UserEntity.USER_ID_COLUMN_NAME} = $testUserId")
        cursor.use {
            assert(cursor.moveToFirst())

            // Verify original columns are intact
            val userIdIndex = cursor.getColumnIndex(UserEntity.USER_ID_COLUMN_NAME)
            val userNameIndex = cursor.getColumnIndex(UserEntity.USER_NAME_COLUMN_NAME)
            val userSelectedIndex = cursor.getColumnIndex(UserEntity.USER_SELECTED_COLUMN_NAME)
            val lastSessionTimestampIndex =
                cursor.getColumnIndex(UserEntity.USER_LAST_SESSION_TIMESTAMP_COLUMN_NAME)

            assertEquals(testUserId, cursor.getLong(userIdIndex))
            assertEquals(testUserName, cursor.getString(userNameIndex))
            assertEquals(testUserSelected, cursor.getInt(userSelectedIndex))

            // Verify new column exists and is NULL
            assertNotNull(
                "Column ${UserEntity.USER_LAST_SESSION_TIMESTAMP_COLUMN_NAME} should exist",
                lastSessionTimestampIndex,
            )
            assert(cursor.isNull(lastSessionTimestampIndex)) {
                "New column should be NULL for existing records"
            }
        }

        migratedDb.close()
    }

    @Test
    @Throws(IOException::class)
    fun migrate1To2_preservesMultipleUsers() {
        // Create database with version 1 schema
        val db = helper.createDatabase(testDatabaseName, 1)

        // Insert multiple test users
        val users =
            listOf(
                Triple(1L, "User One", 1),
                Triple(2L, "User Two", 0),
                Triple(3L, "User Three", 1),
            )

        users.forEach { (id, name, selected) ->
            db.execSQL(
                "INSERT INTO ${UserEntity.USERS_TABLE_NAME} " +
                    "(${UserEntity.USER_ID_COLUMN_NAME}, ${UserEntity.USER_NAME_COLUMN_NAME}, ${UserEntity.USER_SELECTED_COLUMN_NAME}) " +
                    "VALUES ($id, '$name', $selected)",
            )
        }
        db.close()

        // Run migration
        val migratedDb = helper.runMigrationsAndValidate(testDatabaseName, 2, true, MIGRATION_1_2)

        // Verify all users are preserved
        val cursor =
            migratedDb.query("SELECT * FROM ${UserEntity.USERS_TABLE_NAME} ORDER BY ${UserEntity.USER_ID_COLUMN_NAME}")
        cursor.use {
            assertEquals(users.size, cursor.count)

            var index = 0
            while (cursor.moveToNext()) {
                val (expectedId, expectedName, expectedSelected) = users[index]

                val userIdIndex = cursor.getColumnIndex(UserEntity.USER_ID_COLUMN_NAME)
                val userNameIndex = cursor.getColumnIndex(UserEntity.USER_NAME_COLUMN_NAME)
                val userSelectedIndex = cursor.getColumnIndex(UserEntity.USER_SELECTED_COLUMN_NAME)
                val lastSessionTimestampIndex =
                    cursor.getColumnIndex(UserEntity.USER_LAST_SESSION_TIMESTAMP_COLUMN_NAME)

                assertEquals(expectedId, cursor.getLong(userIdIndex))
                assertEquals(expectedName, cursor.getString(userNameIndex))
                assertEquals(expectedSelected, cursor.getInt(userSelectedIndex))
                assert(cursor.isNull(lastSessionTimestampIndex)) {
                    "All migrated records should have NULL lastSessionTimestamp"
                }

                index++
            }
        }

        migratedDb.close()
    }

    @Test
    @Throws(IOException::class)
    fun migrate1To2_allowsInsertingNewColumnValue() {
        // Create database with version 1 schema
        val db = helper.createDatabase(testDatabaseName, 1)
        db.close()

        // Run migration
        val migratedDb = helper.runMigrationsAndValidate(testDatabaseName, 2, true, MIGRATION_1_2)

        // Insert new user with lastSessionTimestamp value
        val testTimestamp = 1234567890L
        migratedDb.execSQL(
            "INSERT INTO ${UserEntity.USERS_TABLE_NAME} " +
                "(${UserEntity.USER_NAME_COLUMN_NAME}, " +
                "${UserEntity.USER_SELECTED_COLUMN_NAME}, " +
                "${UserEntity.USER_LAST_SESSION_TIMESTAMP_COLUMN_NAME}) " +
                "VALUES ('New User', 1, $testTimestamp)",
        )

        // Verify the new column can store values
        val cursor =
            migratedDb.query(
                "SELECT * FROM ${UserEntity.USERS_TABLE_NAME} WHERE ${UserEntity.USER_NAME_COLUMN_NAME} = 'New User'",
            )
        cursor.use {
            assert(cursor.moveToFirst())

            val lastSessionTimestampIndex =
                cursor.getColumnIndex(UserEntity.USER_LAST_SESSION_TIMESTAMP_COLUMN_NAME)
            assertEquals(testTimestamp, cursor.getLong(lastSessionTimestampIndex))
        }

        migratedDb.close()
    }

    @Test
    @Throws(IOException::class)
    fun allMigrationsWork() {
        // Test that all migrations work sequentially from version 1 to current
        helper.createDatabase(testDatabaseName, 1).close()
        helper.runMigrationsAndValidate(
            testDatabaseName,
            2,
            true,
            MIGRATION_1_2,
        )

        // Open the database with Room to verify schema is valid
        val db =
            Room
                .databaseBuilder(
                    InstrumentationRegistry.getInstrumentation().targetContext,
                    SimpleHiitDatabase::class.java,
                    testDatabaseName,
                ).build()

        db.openHelper.writableDatabase.close()
        db.close()
    }
}
