package fr.shining_cat.simplehiit.data.mappers

import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity
import fr.shining_cat.simplehiit.domain.models.Session
import fr.shining_cat.simplehiit.domain.models.User
import javax.inject.Inject

class SessionMapper @Inject constructor() {

    fun convert(sessionEntity: SessionEntity): Session {
        return Session(id = sessionEntity.sessionId, date = sessionEntity.date, duration = sessionEntity.durationMs, users = emptyList())
    }

    fun convert(sessionModel:Session):SessionEntity{
        return SessionEntity(sessionId = sessionModel.id, date = sessionModel.date, durationMs = sessionModel.duration)
    }

}