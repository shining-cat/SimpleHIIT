package fr.shining_cat.simplehiit.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.data.local.database.dao.SessionRecordsDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity

@ExcludeFromJacocoGeneratedReport
@Database(
    entities = [UserEntity::class, SessionEntity::class],
    version = 1
)
abstract class SimpleHiitDatabase : RoomDatabase() {

    abstract fun userDao(): UsersDao

    abstract fun sessionsDao(): SessionRecordsDao

    @ExcludeFromJacocoGeneratedReport
    companion object {
        val SimpleHiitDatabaseName = "everyday_database.db"
    }
}