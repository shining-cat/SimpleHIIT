package fr.shiningcat.simplehiit.domain.session.usecases

import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.RepeatedTest
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
internal class ComposeExercisesListForSessionUseCaseTest : AbstractMockkTest() {
    @RepeatedTest(1000)
    fun `Testing created list is correct size and exercises are only present once and only of selected types`() =
        runTest {
            val testedUseCase =
                ComposeExercisesListForSessionUseCase(
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            val fullListOfExerciseTypes = ExerciseType.entries
            val testNumberOfSelectedTypes =
                Random.nextInt(fullListOfExerciseTypes.size.div(2), fullListOfExerciseTypes.size)
            val randomExerciseTypes =
                fullListOfExerciseTypes.shuffled().take(testNumberOfSelectedTypes)
            val exercisesForSelectedTypesAvailable =
                Exercise.entries
                    .filter { randomExerciseTypes.contains(it.exerciseType) }
                    .size
            // no point in picking too many work periods as we're not testing the "exceeding" case here
            val numberOfWorkPeriodsPerCycle =
                Random.nextInt(
                    1,
                    // leave some margin to ensure maxNumberOfCyclesForTest is larger than 0
                    exercisesForSelectedTypesAvailable.div(2),
                ) // thus we should always have at least 1 cycle
            // now we want to do tests for when there are enough exercises for the total number requested
            val maxNumberOfCyclesForTest =
                (exercisesForSelectedTypesAvailable.toDouble() / numberOfWorkPeriodsPerCycle.toDouble()).toInt()
            val numberOfCycles = Random.nextInt(1, maxNumberOfCyclesForTest)
            val expectedSize = numberOfWorkPeriodsPerCycle * numberOfCycles
            // asserting conditions for the test are met
            assertTrue(expectedSize <= exercisesForSelectedTypesAvailable)
            // calling
            val result =
                testedUseCase.execute(
                    numberOfWorkPeriodsPerCycle = numberOfWorkPeriodsPerCycle,
                    numberOfCycles = numberOfCycles,
                    selectedExerciseTypes = randomExerciseTypes,
                )
            // checking
            assertListMeetsRequirements(
                result = result,
                expectedSize = expectedSize,
                selectedExerciseTypes = randomExerciseTypes,
            )
            // we can't assert unicity as the picking logic takes the exercise type in highest priority,
            // and will refill the picking list with the fresh list if no exercise of this type is available,
            // leading to unpredictable duplicates
        }

    @RepeatedTest(1000)
    fun `Testing created list is correct size and exercises are only of selected types when requested number is too big`() =
        runTest {
            val testedUseCase =
                ComposeExercisesListForSessionUseCase(
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            val fullListOfExerciseTypes = ExerciseType.entries
            val randomNumberOfSelectedTypes = Random.nextInt(1, fullListOfExerciseTypes.size)
            val randomExerciseTypes =
                fullListOfExerciseTypes.shuffled().take(randomNumberOfSelectedTypes)
            val exercisesForSelectedTypesAvailable =
                Exercise.entries
                    .filter { randomExerciseTypes.contains(it.exerciseType) }
                    .size
            val randomNumberOfWorkPeriodsPerCycle = Random.nextInt(1, 8)
            // we want to do tests for when there are too many work periods total for the available number of exercises of selected types
            val minNumberOfCyclesForTest =
                1 + (exercisesForSelectedTypesAvailable.toDouble() / randomNumberOfWorkPeriodsPerCycle.toDouble()).toInt()
            val randomNumberOfCycles =
                Random.nextInt(minNumberOfCyclesForTest, 2 * minNumberOfCyclesForTest)
            val expectedSize = randomNumberOfWorkPeriodsPerCycle * randomNumberOfCycles
            // asserting conditions for the test are met
            assertTrue(expectedSize > exercisesForSelectedTypesAvailable)
            // calling
            val result =
                testedUseCase.execute(
                    numberOfWorkPeriodsPerCycle = randomNumberOfWorkPeriodsPerCycle,
                    numberOfCycles = randomNumberOfCycles,
                    selectedExerciseTypes = randomExerciseTypes,
                )
            // checking
            assertListMeetsRequirements(
                result = result,
                expectedSize = expectedSize,
                selectedExerciseTypes = randomExerciseTypes,
            )
        }

    private fun assertListMeetsRequirements(
        result: List<Exercise>,
        expectedSize: Int,
        selectedExerciseTypes: List<ExerciseType>,
    ) {
        // assert resulting list size
        assertEquals(expectedSize, result.size)
        // assert all exercises selected are of a type in the requested selection
        for (exercise in result.distinct()) {
            assertTrue(selectedExerciseTypes.contains(exercise.exerciseType))
        }
        // asymmetrical exercises should be grouped by 2 and of an even number
        val asymmetricalExercises = result.filter { it.asymmetrical }
        // we should have an even number of asymmetrical exercises inserted
        assertEquals(0, asymmetricalExercises.size % 2)
        for (i in 1 until asymmetricalExercises.size step 2) {
            // assert asymmetricalExercises contains grouped pairs of identical exercises
            assertEquals(asymmetricalExercises[i], asymmetricalExercises[i - 1])
        }
        // assert that non-asymmetrical exercises inserted are always different from the previous in the list
        var checkingAsymmetricalInAPair = false
        for ((index, exercise) in result.withIndex()) {
            val previousExercise = result.getOrNull(index - 1)
            if (checkingAsymmetricalInAPair) {
                assertTrue(exercise.asymmetrical) {
                    "previous exercise $previousExercise was first asymmetrical in an expected pair" +
                        " but this one is not: $exercise"
                }
            }
            if (exercise.asymmetrical) {
                if (checkingAsymmetricalInAPair) {
                    assertTrue(exercise == previousExercise) {
                        "previous exercise $previousExercise was first asymmetrical in an expected" +
                            " pair but this one does not match: $exercise"
                    }
                    checkingAsymmetricalInAPair = false
                } else {
                    // we picked the first asymmetrical of a pair, it should not be the same exercise as the previous one
                    assertTrue(exercise != previousExercise) {
                        "Two identical exercises found: $exercise and $previousExercise"
                    }
                    checkingAsymmetricalInAPair = true
                }
            } else {
                // non asymmetrical should always be different from precedent one
                assertTrue(exercise != previousExercise) {
                    "Two identical exercises found: $exercise and $previousExercise"
                }
            }
        }
    }
}
