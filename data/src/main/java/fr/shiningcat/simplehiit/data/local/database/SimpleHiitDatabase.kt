package fr.shiningcat.simplehiit.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.shiningcat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport
import fr.shiningcat.simplehiit.data.local.database.dao.SessionRecordsDao
import fr.shiningcat.simplehiit.data.local.database.dao.UsersDao
import fr.shiningcat.simplehiit.data.local.database.entities.SessionEntity
import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity

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