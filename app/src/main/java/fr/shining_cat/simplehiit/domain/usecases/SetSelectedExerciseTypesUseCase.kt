package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.utils.HiitLogger

class SetSelectedExerciseTypesUseCase(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(listOfSelectedExerciseTypes: List<ExerciseType>) {
        simpleHiitRepository.setExercisesTypesSelected(listOfSelectedExerciseTypes)
    }
}