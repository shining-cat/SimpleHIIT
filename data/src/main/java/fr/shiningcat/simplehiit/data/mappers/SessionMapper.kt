package fr.shiningcat.simplehiit.data.mappers

import fr.shiningcat.simplehiit.data.local.database.entities.SessionEntity
import fr.shiningcat.simplehiit.domain.common.models.SessionRecord

/**
 * For Koin: called directly as SessionMapper() in dataModule
 */
class SessionMapper {
    fun convert(sessionEntity: SessionEntity): SessionRecord =
        SessionRecord(
            id = sessionEntity.sessionId,
            timeStamp = sessionEntity.timeStamp,
            durationMs = sessionEntity.durationMs,
            usersIds = listOf(sessionEntity.userId),
        )

    fun convert(sessionRecordModel: SessionRecord): List<SessionEntity> =
        sessionRecordModel.usersIds.map { userId ->
            SessionEntity(
                sessionId = sessionRecordModel.id,
                userId = userId,
                timeStamp = sessionRecordModel.timeStamp,
                durationMs = sessionRecordModel.durationMs,
            )
        }
}
