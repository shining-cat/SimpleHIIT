package fr.shining_cat.simplehiit.data.mappers

import fr.shining_cat.simplehiit.domain.common.models.SessionRecord
import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity
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