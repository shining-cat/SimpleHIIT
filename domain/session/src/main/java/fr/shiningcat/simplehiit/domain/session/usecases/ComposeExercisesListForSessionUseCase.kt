package fr.shiningcat.simplehiit.domain.session.usecases

import androidx.annotation.VisibleForTesting
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.di.DefaultDispatcher
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ComposeExercisesListForSessionUseCase
    @Inject
    constructor(
        @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
        private val hiitLogger: HiitLogger,
    ) {
        suspend fun execute(
            numberOfWorkPeriodsPerCycle: Int,
            numberOfCycles: Int,
            selectedExerciseTypes: List<ExerciseType>,
        ): List<Exercise> =
            withContext(defaultDispatcher) {
                hiitLogger.d(
                    tag = "ComposeExercisesListForSessionUseCase",
                    msg =
                        "execute::START: numberOfWorkPeriodsPerCycle = $numberOfWorkPeriodsPerCycle numberOfCycles = $numberOfCycles selectedExerciseTypes = $selectedExerciseTypes",
                )
                val wantedNumberOfExercises = numberOfWorkPeriodsPerCycle * numberOfCycles

                val exercisesSourceList =
                    buildSourceListForSelectedTypes(
                        selectedExerciseTypes,
                        wantedNumberOfExercises,
                    )
                //
                val resultListOfExercises = mutableListOf<Exercise>()
                while (resultListOfExercises.size < wantedNumberOfExercises) {
                    // we loop on types to ensure variety of resulting list:
                    pickLoop@ for (type in selectedExerciseTypes) {
                        hiitLogger.d(
                            tag = "ComposeExercisesListForSessionUseCase",
                            msg = "Loop:: picking type: $type",
                        )
                        val pickingListForType =
                            buildPickingListForOneType(
                                typeToPick = type,
                                isLastExerciseToPick = resultListOfExercises.size == wantedNumberOfExercises - 1,
                                exercisesSourceList = exercisesSourceList,
                                lastPickedExercise = resultListOfExercises.lastOrNull(),
                            )
                        // pick a random exercise in the prepared list
                        val exercisePicked = pickingListForType.random()
                        // remove exercise from source list to limit repetition, if the exercise was picked from a fresh list, it won't be present so calling remove will just do nothing
                        exercisesSourceList.remove(exercisePicked)
                        resultListOfExercises.add(exercisePicked)
                        // add asymmetrical exercises twice as they have to be done once for each side
                        if (exercisePicked.asymmetrical) resultListOfExercises.add(exercisePicked)
                        if (resultListOfExercises.size == wantedNumberOfExercises) {
                            // we have reached the expected number of exercises: stop looping
                            break@pickLoop
                        }
                    }
                }
                hiitLogger.d(
                    tag = "ComposeExercisesListForSessionUseCase",
                    msg =
                        "execute::DONE::expected: ${numberOfWorkPeriodsPerCycle * numberOfCycles}, " +
                            "size is ${resultListOfExercises.size} -- list = $resultListOfExercises",
                )
                resultListOfExercises.toList()
            }

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun buildSourceListForSelectedTypes(
            selectedExerciseTypes: List<ExerciseType>,
            wantedNumberOfExercises: Int,
        ): MutableList<Exercise> {
            val exercisesOfSelectedTypesSourceList =
                Exercise.entries.filter { selectedExerciseTypes.contains(it.exerciseType) }

            val exercisesSourceList = exercisesOfSelectedTypesSourceList.toMutableList()
            // todo: display warning if wantedNumberOfExercises < selectedExerciseTypes.size: we won't have an exercise for each type in the resulting list
            while (wantedNumberOfExercises > numberOfAvailableExercises(exercisesSourceList)) {
                hiitLogger.d(
                    tag = "ComposeExercisesListForSessionUseCase",
                    msg =
                        "buildSourceListForSelectedTypes:: build a list for a total of $wantedNumberOfExercises exercises," +
                            " when selected list contains ${exercisesSourceList.size}." +
                            " -> Adding the whole pack of exercises again in source list to pick from",
                )
                // TODO: display warning in presentation layer for exercises duplication
                exercisesSourceList.addAll(exercisesOfSelectedTypesSourceList)
            }
            return exercisesSourceList
        }

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun numberOfAvailableExercises(exercisesList: List<Exercise>): Int {
            val numberOfAsymmetricalExercises = exercisesList.filter { it.asymmetrical }.size
            val numberOfSymmetricalExercises = exercisesList.filter { !it.asymmetrical }.size
            val realTotalNumberOfAvailableExercises =
                numberOfSymmetricalExercises.plus(2.times(numberOfAsymmetricalExercises))
            return realTotalNumberOfAvailableExercises
        }

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun buildPickingListForOneType(
            typeToPick: ExerciseType,
            isLastExerciseToPick: Boolean,
            exercisesSourceList: MutableList<Exercise>,
            lastPickedExercise: Exercise?,
        ): List<Exercise> =
            if (isLastExerciseToPick) {
                // we're picking the last item
                hiitLogger.d(
                    tag = "ComposeExercisesListForSessionUseCase",
                    msg =
                        "buildPickingListForType::preparing picking list for last exercise of type: $typeToPick," +
                            " it has to be symmetrical and different from the last picked one",
                )
                exercisesSourceList
                    .filter { it.exerciseType == typeToPick && it != lastPickedExercise && !it.asymmetrical }
                    .ifEmpty {
                        hiitLogger.e(
                            tag = "ComposeExercisesListForSessionUseCase",
                            msg =
                                "buildPickingListForType::filtering for last item to pick" +
                                    " is an empty list, returning a fresh filtered list",
                        )
                        Exercise.entries.filter {
                            it.exerciseType == typeToPick &&
                                !it.asymmetrical &&
                                it != lastPickedExercise
                        }
                    }
            } else {
                hiitLogger.d(
                    tag = "ComposeExercisesListForSessionUseCase",
                    msg =
                        "buildPickingListForType::preparing picking list for exercise" +
                            " of type: $typeToPick, it has to be different from the last picked one",
                )
                exercisesSourceList
                    .filter { it.exerciseType == typeToPick && it != lastPickedExercise }
                    .ifEmpty {
                        hiitLogger.e(
                            tag = "ComposeExercisesListForSessionUseCase",
                            msg =
                                "buildPickingListForType::filtering for an item to pick" +
                                    " is an empty list, returning a fresh filtered list",
                        )
                        Exercise.entries.filter {
                            it.exerciseType == typeToPick &&
                                it != lastPickedExercise
                        }
                    }
            }
    }
