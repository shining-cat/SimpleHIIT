package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.utils.HiitLogger

class DeleteSessionsForUserUseCase(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(userId: Long): Output<Int> {
        return simpleHiitRepository.deleteSessionsForUser(userId)
    }
}