package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.domain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class SetSelectedExerciseTypesUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(listOfSelectedExerciseTypes: List<ExerciseTypeSelected>) {
        val onlySelectedExerciseTypes = listOfSelectedExerciseTypes.filter { it.selected }.map { it.type }
        simpleHiitRepository.setExercisesTypesSelected(onlySelectedExerciseTypes)
    }
}