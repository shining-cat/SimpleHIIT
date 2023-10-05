package fr.shiningcat.simplehiit.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import fr.shiningcat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport
import fr.shiningcat.simplehiit.data.local.database.entities.SessionEntity.Companion.sessionsTableName

@ExcludeFromJacocoGeneratedReport
@Entity(
    tableName = sessionsTableName,
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = arrayOf(UserEntity.userIdColumnName),
            childColumns = arrayOf(SessionEntity.userIdColumnName),
            onDelete = ForeignKey.CASCADE
        )]
)
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = sessionIdColumnName) val sessionId: Long = 0L,
    @ColumnInfo(name = userIdColumnName, index = true) val userId: Long,
    @ColumnInfo(name = timeStampColumnName) val timeStamp: Long,
    @ColumnInfo(name = durationColumnName) val durationMs: Long
) {

    companion object {
        const val sessionsTableName = "simple_hiit_sessions"
        //
        const val sessionIdColumnName = "session_id"
        const val userIdColumnName = "user_id"
        const val timeStampColumnName = "timestamp"
        const val durationColumnName = "duration"
    }
}