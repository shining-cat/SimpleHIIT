package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
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