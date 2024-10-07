package fr.shiningcat.simplehiit.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import fr.shiningcat.simplehiit.commonutils.annotations.ExcludeFromJacocoGeneratedReport
import fr.shiningcat.simplehiit.data.local.database.entities.SessionEntity.Companion.SESSIONS_TABLE_NAME

@ExcludeFromJacocoGeneratedReport
@Entity(
    tableName = SESSIONS_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = arrayOf(UserEntity.USER_ID_COLUMN_NAME),
            childColumns = arrayOf(SessionEntity.USER_ID_COLUMN_NAME),
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SESSION_ID_COLUMN_NAME)
    val sessionId: Long = 0L,
    @ColumnInfo(name = USER_ID_COLUMN_NAME, index = true) val userId: Long,
    @ColumnInfo(name = TIMESTAMP_COLUMN_NAME) val timeStamp: Long,
    @ColumnInfo(name = DURATION_COLUMN_NAME) val durationMs: Long,
) {
    companion object {
        const val SESSIONS_TABLE_NAME = "simple_hiit_sessions"

        //
        const val SESSION_ID_COLUMN_NAME = "session_id"
        const val USER_ID_COLUMN_NAME = "user_id"
        const val TIMESTAMP_COLUMN_NAME = "timestamp"
        const val DURATION_COLUMN_NAME = "duration"
    }
}
