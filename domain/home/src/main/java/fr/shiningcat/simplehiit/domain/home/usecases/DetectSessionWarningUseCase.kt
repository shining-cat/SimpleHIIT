package fr.shiningcat.simplehiit.domain.home.usecases

import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.LaunchSessionWarning
import fr.shiningcat.simplehiit.domain.common.models.SessionSettings

class DetectSessionWarningUseCase {
    fun execute(sessionSettings: SessionSettings): LaunchSessionWarning? {
        // First priority: Check for no users selected
        // If no user is selected, no point checking exercise-related warnings
        if (sessionSettings.users.none { it.selected }) {
            return LaunchSessionWarning.NO_USER_SELECTED
        }

        // Calculate session parameters for exercise-related checks
        val wantedNumberOfExercises =
            sessionSettings.numberOfWorkPeriods * sessionSettings.numberCumulatedCycles
        val selectedTypes =
            sessionSettings.exerciseTypes.filter { it.selected }.map { it.type }
        val availableExercisesCount = getAvailableExercisesCount(selectedTypes)

        // Second priority: Check if session is too short
        // (fewer exercises needed than selected types, so some types will be skipped)
        if (wantedNumberOfExercises < selectedTypes.size) {
            return LaunchSessionWarning.SKIPPED_EXERCISE_TYPES
        }

        // Third priority: Check if session is too long
        // (more exercises needed than available, so some will be duplicated)
        // Note: This is mutually exclusive with SKIPPED_EXERCISE_TYPES
        if (wantedNumberOfExercises > availableExercisesCount) {
            return LaunchSessionWarning.DUPLICATED_EXERCISES
        }

        // No warnings detected
        return null
    }

    private fun getAvailableExercisesCount(selectedTypes: List<ExerciseType>): Int {
        val exercises =
            Exercise.entries.filter {
                selectedTypes.contains(it.exerciseType)
            }
        val asymmetricalCount = exercises.count { it.asymmetrical }
        val symmetricalCount = exercises.count { !it.asymmetrical }
        // Asymmetrical exercises count as 2 (one for each side)
        return symmetricalCount + (2 * asymmetricalCount)
    }
}
