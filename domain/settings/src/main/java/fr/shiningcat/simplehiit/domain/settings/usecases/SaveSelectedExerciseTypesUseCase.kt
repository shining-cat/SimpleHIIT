package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SaveSelectedExerciseTypesUseCase(
    private val settingsRepository: SettingsRepository,
    private val defaultDispatcher: CoroutineDispatcher,
    private val logger: HiitLogger,
) {
    suspend fun execute(listOfSelectedExerciseTypes: List<ExerciseTypeSelected>) {
        withContext(defaultDispatcher) {
            val onlySelectedExerciseTypes =
                listOfSelectedExerciseTypes.filter { it.selected }.map { it.type }
            settingsRepository.setExercisesTypesSelected(onlySelectedExerciseTypes)
        }
    }
}
