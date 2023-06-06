package fr.shining_cat.simplehiit.domain.settings.usecases

import fr.shining_cat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.commonutils.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveSelectedExerciseTypesUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(listOfSelectedExerciseTypes: List<ExerciseTypeSelected>) {
        withContext(defaultDispatcher) {
            val onlySelectedExerciseTypes =
                listOfSelectedExerciseTypes.filter { it.selected }.map { it.type }
            simpleHiitRepository.setExercisesTypesSelected(onlySelectedExerciseTypes)
        }
    }
}