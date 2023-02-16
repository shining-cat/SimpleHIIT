package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.utils.HiitLogger

class SetBeepSoundUseCase(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(active: Boolean) {
        simpleHiitRepository.setBeepSound(active)
    }
}