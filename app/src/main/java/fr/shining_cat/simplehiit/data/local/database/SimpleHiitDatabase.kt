package fr.shining_cat.simplehiit.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.shining_cat.simplehiit.data.local.database.dao.SessionsDao
import fr.shining_cat.simplehiit.data.local.database.dao.SessionsUsersLinkDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity
import fr.shining_cat.simplehiit.data.local.database.entities.SessionsUsersLinkEntity
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity

@Database(
    entities = [UserEntity::class, SessionEntity::class, SessionsUsersLinkEntity::class],
    version = 1
)
abstract class SimpleHiitDatabase : RoomDatabase() {

    abstract fun userDao(): UsersDao

    abstract fun sessionsDao(): SessionsDao

    abstract fun sessionsUsersLinkDao(): SessionsUsersLinkDao


    companion object {
        val SimpleHiitDatabaseName = "everyday_database.db"
    }
}