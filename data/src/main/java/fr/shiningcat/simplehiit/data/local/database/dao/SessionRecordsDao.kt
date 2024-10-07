package fr.shiningcat.simplehiit.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.shiningcat.simplehiit.data.local.database.entities.SessionEntity

@Dao
abstract class SessionRecordsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(sessions: List<SessionEntity>): List<Long>

    @Query("SELECT * FROM ${SessionEntity.SESSIONS_TABLE_NAME} WHERE ${SessionEntity.USER_ID_COLUMN_NAME} = :userId")
    abstract suspend fun getSessionsForUser(userId: Long): List<SessionEntity>

    @Query("DELETE FROM ${SessionEntity.SESSIONS_TABLE_NAME} WHERE ${SessionEntity.USER_ID_COLUMN_NAME} = :userId")
    abstract suspend fun deleteForUser(userId: Long): Int
}
