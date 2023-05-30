package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.di.DefaultDispatcher
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.utils.HiitLogger
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