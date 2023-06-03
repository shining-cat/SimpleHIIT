package fr.shining_cat.simplehiit.commondomain.usecases

import fr.shining_cat.simplehiit.commondomain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import javax.inject.Inject

class ToggleExerciseTypeInListUseCase @Inject constructor(
    private val hiitLogger: HiitLogger
) {

    fun execute(
        currentList: List<ExerciseTypeSelected>,
        exerciseTypeToToggle: ExerciseTypeSelected
    ): List<ExerciseTypeSelected> {
        return currentList.map {
            if (it.type == exerciseTypeToToggle.type) it.copy(selected = !it.selected)
            else it
        }
    }

}