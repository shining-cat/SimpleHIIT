package fr.shining_cat.simplehiit.data.local.database.entities

import androidx.room.*
import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity.Companion.sessionsTableName

@ExcludeFromJacocoGeneratedReport
@Entity(tableName = sessionsTableName)
data class SessionEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = sessionIdColumnName) val sessionId: Long = 0L,
    @ColumnInfo(name = timeStampColumnName) val date: Long, //todo: check if we go for sql.date here
    @ColumnInfo(name = durationColumnName) val durationMs: Long
) {

    companion object {
        const val sessionsTableName = "simple_hiit_sessions"
        //
        const val sessionIdColumnName = "sessionId"
        const val timeStampColumnName = "timestamp"
        const val durationColumnName = "duration"
    }
}