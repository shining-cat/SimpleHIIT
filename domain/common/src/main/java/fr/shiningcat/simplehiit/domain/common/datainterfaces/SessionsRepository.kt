package fr.shiningcat.simplehiit.domain.common.datainterfaces

import fr.shiningcat.simplehiit.commonutils.annotations.ExcludeFromJacocoGeneratedReport
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.SessionRecord
import fr.shiningcat.simplehiit.domain.common.models.User

@ExcludeFromJacocoGeneratedReport
interface SessionsRepository {
    suspend fun insertSessionRecord(sessionRecord: SessionRecord): Output<Int>

    suspend fun getSessionRecordsForUser(user: User): Output<List<SessionRecord>>

    suspend fun deleteSessionRecordsForUser(userId: Long): Output<Int>
}
