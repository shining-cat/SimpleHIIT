package fr.shining_cat.simplehiit.data.local.database.dao

import androidx.room.*
import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity

@Dao
abstract class SessionRecordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(sessions: List<SessionEntity>): List<Long>

    @Query("SELECT * FROM ${SessionEntity.sessionsTableName} WHERE ${SessionEntity.userIdColumnName} = :userId")
    abstract suspend fun getSessionsForUser(userId: Long): List<SessionEntity>

    @Query("DELETE FROM ${SessionEntity.sessionsTableName} WHERE ${SessionEntity.userIdColumnName} = :userId")
    abstract suspend fun deleteForUser(userId: Long): Int

}