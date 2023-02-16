package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.Session
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.utils.HiitLogger

class InsertSessionUseCase(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(session: Session) {
        simpleHiitRepository.insertSession(session)
    }
}