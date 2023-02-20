package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.models.Exercise
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.utils.HiitLogger

class ComposeExercisesListForSessionUseCase(
    private val hiitLogger: HiitLogger
) {

    suspend fun execute(
        numberOfWorkPeriodsPerCycle: Int,
        numberOfCycles: Int,
        selectedExerciseTypes: List<ExerciseType>
    ): List<Exercise> {
        val exercisesOfSelectedTypesSourceList = listOfExercisesOfSelectedTypes(selectedExerciseTypes)
        val exercisesSourceList = exercisesOfSelectedTypesSourceList.toMutableList()
        val wantedNumberOfExercises = numberOfWorkPeriodsPerCycle * numberOfCycles
        while (wantedNumberOfExercises > numberOfAvailableExercises(exercisesSourceList)) {
            hiitLogger.d(
                "ComposeExercisesListForSessionUseCase",
                "execute:: requested a list for $numberOfCycles cycles of $numberOfWorkPeriodsPerCycle " +
                        "work periods, for a total of $wantedNumberOfExercises exercises, when selected list contains ${exercisesSourceList.size}." +
                        "Adding the whole pack of exercises again in list to pick from"
            )
            //TODO: display warning in presentation layer for exercises duplication
            exercisesSourceList.addAll(exercisesOfSelectedTypesSourceList)
        }
        //
        val listOfExercises = mutableListOf<Exercise>()
        while (listOfExercises.size < wantedNumberOfExercises) {
            pickLoop@ for (type in selectedExerciseTypes) {
                //order of exercises types set in ExerciseType is what will determine order in session
                val exercisesForType =
                    if (listOfExercises.size == wantedNumberOfExercises - 1) {
                        if (exercisesSourceList.none { !it.asymmetrical }) {
                            // only one spot left, but all remaining exercises in available list are asymmetrical
                            // adding whole pack one last time to allow for the last picking to not block the loop
                            exercisesSourceList.addAll(exercisesOfSelectedTypesSourceList)
                        }
                        //only one spot left, we need to pick a non-asymmetrical exercise
                        exercisesSourceList.filter { it.exerciseType == type && !it.asymmetrical }
                    } else {
                        exercisesSourceList.filter { it.exerciseType == type }
                    }
                //if no exercise for this type is left (or if the only one left for a single spot is an asymmetrical), skip this type and continue to next one
                if (exercisesForType.isEmpty()) {
                    hiitLogger.d("ComposeExercisesListForSessionUseCase", "skipped")
                    continue@pickLoop
                }
                val exercisePicked = exercisesForType.random()
                exercisesSourceList.remove(exercisePicked) // remove exercise from source list to limit repetition
                listOfExercises.add(exercisePicked)
                if (exercisePicked.asymmetrical) listOfExercises.add(exercisePicked) // add asymmetrical exercises twice as they have to be done twice
                if (listOfExercises.size == wantedNumberOfExercises) break@pickLoop //we have reached the expected number of exercises: stop looping
            }
        }
        hiitLogger.d(
            "ComposeExercisesListForSessionUseCase",
            "execute::DONE::expected: ${numberOfWorkPeriodsPerCycle * numberOfCycles}, size is ${listOfExercises.size} -- list = $listOfExercises"
        )
        return listOfExercises.toList()
    }

    private fun listOfExercisesOfSelectedTypes(selectedExerciseTypes: List<ExerciseType>): List<Exercise> =
        Exercise.values().toList().filter { selectedExerciseTypes.contains(it.exerciseType) }

    private fun numberOfAvailableExercises(exercisesList: List<Exercise>): Int {
        val numberOfAsymmetricalExercises = exercisesList.filter { it.asymmetrical }.size
        val numberOfSymmetricalExercises = exercisesList.filter { !it.asymmetrical }.size
        val realTotalNumberOfAvailableExercises =
            numberOfSymmetricalExercises + 2 * numberOfAsymmetricalExercises
        return realTotalNumberOfAvailableExercises
    }

}