package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class ToggleExerciseTypeInListUseCase @Inject constructor(
    private val hiitLogger: HiitLogger
) {

    fun execute(currentList: List<ExerciseTypeSelected>, exerciseTypeToToggle: ExerciseTypeSelected): List<ExerciseTypeSelected> {
        return currentList.map {
            if (it.type == exerciseTypeToToggle.type) it.copy(selected = !it.selected)
            else it
        }
    }

}