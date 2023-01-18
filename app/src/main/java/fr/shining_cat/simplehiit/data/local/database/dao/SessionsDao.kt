package fr.shining_cat.simplehiit.data.local.database.dao

import androidx.room.*
import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity
import fr.shining_cat.simplehiit.data.local.database.entities.SessionsUsersLinkEntity
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity.Companion.userIdColumnName
import kotlinx.coroutines.flow.Flow

@ExcludeFromJacocoGeneratedReport
@Dao
abstract class SessionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(session: SessionEntity):Long

    @Query("DELETE FROM ${SessionEntity.sessionsTableName} WHERE ${SessionEntity.sessionIdColumnName} = :sessionId")
    abstract suspend fun delete(sessionId: Long): Int


    @Query("SELECT * FROM ${SessionEntity.sessionsTableName} WHERE ${SessionEntity.sessionIdColumnName} IN (:sessionsIds)")
    abstract suspend fun getSessions(sessionsIds: List<Long>): List<SessionEntity>

}