package fr.shiningcat.simplehiit.domain.session.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SessionsRepository
import fr.shiningcat.simplehiit.domain.common.models.SessionRecord
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class InsertSessionUseCase(
    private val sessionsRepository: SessionsRepository,
    private val updateUsersLastSessionTimestampUseCase: UpdateUsersLastSessionTimestampUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    private val logger: HiitLogger,
) {
    suspend fun execute(sessionRecord: SessionRecord): Output<Int> =
        withContext(defaultDispatcher) {
            val insertResult = sessionsRepository.insertSessionRecord(sessionRecord)

            if (insertResult is Output.Success) {
                updateUsersLastSessionTimestampUseCase.execute(
                    userIds = sessionRecord.usersIds,
                    timestamp = sessionRecord.timeStamp,
                )
            }

            insertResult
        }
}
