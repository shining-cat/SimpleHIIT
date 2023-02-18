package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.models.Exercise
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.utils.HiitLogger

class ComposeExercisesListForSessionUseCase(
    private val hiitLogger: HiitLogger
) {

    suspend fun execute(numberOfWorkPeriodsPerCycle: Int, numberOfCycles: Int): List<Exercise> {
        val listOfExercises = mutableListOf<Exercise>()
        val exerciseTypesList = ExerciseType.values().toList()
        val exercisesList = Exercise.values().toMutableList()
        val totalWantedNumberOfExercises = numberOfWorkPeriodsPerCycle * numberOfCycles
        while (totalWantedNumberOfExercises > exercisesList.size) {
            hiitLogger.d(
                "ComposeExercisesListForSessionUseCase",
                "execute:: requested a list for $numberOfCycles cycles of $numberOfWorkPeriodsPerCycle " +
                        "work periods, for a total of $totalWantedNumberOfExercises exercises, when total list contains ${exercisesList.size}." +
                        "Adding the whole pack of exercises again in list to pick from"
            )
            //TODO: display warning in presentation layer for exercises duplication
            exercisesList.addAll(Exercise.values().toList())
        }
        while (listOfExercises.size < totalWantedNumberOfExercises) {
            pickLoop@ for (type in exerciseTypesList) { //order of exercises types set in ExerciseType is what will determine order in session
                val exercisesForType = if(listOfExercises.size == totalWantedNumberOfExercises - 1){
                    //only one spot left, we need to exclude asymmetrical exercises
                    exercisesList.filter { it.exerciseType == type && !it.asymmetrical }
                } else{
                    exercisesList.filter { it.exerciseType == type }
                }
                if (exercisesForType.isEmpty()) continue@pickLoop //if no exercise for this type is left, skip this type and continue to next one
                val exercisePicked = exercisesForType.random()
                exercisesList.remove(exercisePicked) // remove exercise from source list to avoid repetition
                listOfExercises.add(exercisePicked)
                if (exercisePicked.asymmetrical) listOfExercises.add(exercisePicked) // add asymmetrical exercises twice as they have to be done twice
                if(listOfExercises.size == totalWantedNumberOfExercises) break@pickLoop //we have reached the expected number of exercises: stop looping
            }
        }
        hiitLogger.d(
            "ComposeExercisesListForSessionUseCase",
            "execute::DONE::expected: ${numberOfWorkPeriodsPerCycle * numberOfCycles}, size is ${listOfExercises.size} -- list = $listOfExercises"
        )
        return listOfExercises.toList()
    }

}