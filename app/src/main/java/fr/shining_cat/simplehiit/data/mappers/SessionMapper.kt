package fr.shining_cat.simplehiit.data.mappers

import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity
import fr.shining_cat.simplehiit.domain.models.Session
import javax.inject.Inject

class SessionMapper @Inject constructor() {

    fun convert(sessionEntity: SessionEntity): Session {
        return Session(
            id = sessionEntity.sessionId,
            timeStamp = sessionEntity.timeStamp,
            durationMs = sessionEntity.durationMs,
            usersIds = listOf(sessionEntity.userId)
        )
    }

    fun convert(sessionModel: Session): List<SessionEntity> {
        return sessionModel.usersIds.map { userId ->
            SessionEntity(
                sessionId = sessionModel.id,
                userId = userId,
                timeStamp = sessionModel.timeStamp,
                durationMs = sessionModel.durationMs
            )
        }
    }

}