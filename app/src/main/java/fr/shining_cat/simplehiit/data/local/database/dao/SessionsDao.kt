package fr.shining_cat.simplehiit.data.local.database.dao

import androidx.room.*
import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity

@ExcludeFromJacocoGeneratedReport
@Dao
abstract class SessionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(sessions: List<SessionEntity>):List<Long>

    @Query("DELETE FROM ${SessionEntity.sessionsTableName} WHERE ${SessionEntity.sessionIdColumnName} = :sessionId")
    abstract suspend fun delete(sessionId: Long): Int

    @Query("SELECT * FROM ${SessionEntity.sessionsTableName} WHERE ${SessionEntity.userIdColumnName} = :userId")
    abstract suspend fun getSessionsForUser(userId: Long): List<SessionEntity>

}