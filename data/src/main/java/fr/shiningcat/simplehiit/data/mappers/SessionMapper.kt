package fr.shiningcat.simplehiit.data.mappers

import fr.shiningcat.simplehiit.data.local.database.entities.SessionEntity
import fr.shiningcat.simplehiit.domain.common.models.SessionRecord
import javax.inject.Inject

class SessionMapper @Inject constructor() {

    fun convert(sessionEntity: SessionEntity): SessionRecord {
        return SessionRecord(
            id = sessionEntity.sessionId,
            timeStamp = sessionEntity.timeStamp,
            durationMs = sessionEntity.durationMs,
            usersIds = listOf(sessionEntity.userId)
        )
    }

    fun convert(sessionRecordModel: SessionRecord): List<SessionEntity> {
        return sessionRecordModel.usersIds.map { userId ->
            SessionEntity(
                sessionId = sessionRecordModel.id,
                userId = userId,
                timeStamp = sessionRecordModel.timeStamp,
                durationMs = sessionRecordModel.durationMs
            )
        }
    }
}
