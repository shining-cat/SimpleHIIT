package fr.shining_cat.simplehiit.data.local.database.dao

import androidx.room.*
import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity
import fr.shining_cat.simplehiit.data.local.database.entities.SessionsUsersLinkEntity
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity.Companion.userIdColumnName
import kotlinx.coroutines.flow.Flow

@ExcludeFromJacocoGeneratedReport
@Dao
abstract class SessionsUsersLinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(sessionsUsersLinks: List<SessionsUsersLinkEntity>):Array<Long>

    @Query("DELETE FROM ${SessionsUsersLinkEntity.sessionsUsersLinksTableName} WHERE ${SessionsUsersLinkEntity.sessionUserLinkIdColumnName} = :sessionsUsersLinkId")
    abstract suspend fun deleteByLinkId(sessionsUsersLinkId: Long): Int

    @Query("SELECT * FROM ${SessionsUsersLinkEntity.sessionsUsersLinksTableName} WHERE ${SessionsUsersLinkEntity.userIdColumnName} = :userId")
    abstract suspend fun getSessionsUsersLinksForUser(userId: Long): List<SessionsUsersLinkEntity>

    @Query("SELECT * FROM ${SessionsUsersLinkEntity.sessionsUsersLinksTableName} WHERE ${SessionsUsersLinkEntity.sessionIdColumnName} = :sessionId")
    abstract suspend fun getSessionsUsersLinksForSession(sessionId: Long): List<SessionsUsersLinkEntity>

}