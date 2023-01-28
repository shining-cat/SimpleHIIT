package fr.shining_cat.simplehiit.data.local.database.entities

import androidx.room.*
import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity.Companion.sessionsTableName

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
    @ColumnInfo(name = userIdColumnName) val userId: Long,
    @ColumnInfo(name = timeStampColumnName) val date: Long, //todo: check if we go for sql.date here
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