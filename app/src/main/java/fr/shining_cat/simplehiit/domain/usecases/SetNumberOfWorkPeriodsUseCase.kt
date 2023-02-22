package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class SetNumberOfWorkPeriodsUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(number: Int) {
        simpleHiitRepository.setNumberOfWorkPeriods(number)
    }
}