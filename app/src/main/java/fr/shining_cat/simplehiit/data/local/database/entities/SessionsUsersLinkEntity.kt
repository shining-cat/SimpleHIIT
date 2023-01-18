package fr.shining_cat.simplehiit.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.data.local.database.entities.SessionsUsersLinkEntity.Companion.sessionsUsersLinksTableName

@ExcludeFromJacocoGeneratedReport
@Entity(
    tableName = sessionsUsersLinksTableName,
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = arrayOf(UserEntity.userIdColumnName),
            childColumns = arrayOf(SessionsUsersLinkEntity.userIdColumnName),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = arrayOf(SessionEntity.sessionIdColumnName),
            childColumns = arrayOf(SessionsUsersLinkEntity.sessionIdColumnName),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SessionsUsersLinkEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = sessionUserLinkIdColumnName) val linkId: Long = 0L,
    @ColumnInfo(name = userIdColumnName) val sessionId: Long,
    @ColumnInfo(name = sessionIdColumnName) val userId: Long
) {

    companion object {
        const val sessionsUsersLinksTableName = "simple_hiit_sessions_users_links"
        //
        const val sessionUserLinkIdColumnName = "sessionuserlinkid"
        const val userIdColumnName = "userid"
        const val sessionIdColumnName = "sessionid"
    }
}