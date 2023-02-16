package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.SimpleHiitSettings
import fr.shining_cat.simplehiit.domain.models.TotalRepetitionsSetting
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.flow.Flow

class GetTotalRepetitionsSettingsUseCase(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val simpleHiitLogger: HiitLogger
) {

    fun execute(): Flow<TotalRepetitionsSetting> {
        return simpleHiitRepository.getTotalRepetitionsSetting()
    }
}