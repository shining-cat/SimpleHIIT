package fr.shining_cat.simplehiit.data.mappers

import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity
import fr.shining_cat.simplehiit.domain.models.Session
import fr.shining_cat.simplehiit.domain.models.User
import javax.inject.Inject

class SessionMapper @Inject constructor() {

    fun convert(sessionEntity: SessionEntity): Session {
        return Session(
            id = sessionEntity.sessionId,
            date = sessionEntity.date,
            duration = sessionEntity.durationMs,
            usersIds = listOf(sessionEntity.userId)
        )
    }

    fun convert(sessionModel: Session): List<SessionEntity> {
        return sessionModel.usersIds.map { userId ->
            SessionEntity(
                sessionId = sessionModel.id,
                userId = userId,
                date = sessionModel.date,
                durationMs = sessionModel.duration
            )
        }
    }

}