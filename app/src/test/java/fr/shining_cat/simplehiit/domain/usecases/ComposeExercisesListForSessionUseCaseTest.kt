package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.models.Exercise
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import java.util.*
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
internal class ComposeExercisesListForSessionUseCaseTest : AbstractMockkTest() {

    @RepeatedTest(100)
    fun `Testing created list is correct size and exercises are only present once and only of selected types`() =
        runTest {
            val testedUseCase = ComposeExercisesListForSessionUseCase(
                defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                hiitLogger = mockHiitLogger
            )
            val fullListOfExerciseTypes = ExerciseType.values().toList()
            val testNumberOfSelectedTypes = Random.nextInt(1, fullListOfExerciseTypes.size)
            val testSelectedExercises =
                fullListOfExerciseTypes.shuffled().take(testNumberOfSelectedTypes)
            val exercisesForSelectedTypesAvailable = Exercise.values().toList()
                .filter { testSelectedExercises.contains(it.exerciseType) }.size
            //no point in picking too many work periods as we're not testing the "exceeding" case here
            val numberOfWorkPeriodsPerCycle = Random.nextInt(
                1,
                exercisesForSelectedTypesAvailable
            )//thus we should always have at least 1 cycle
            //now we want to do tests for when there are enough exercises for the total number requested
            val maxNumberOfCyclesForTest =
                (exercisesForSelectedTypesAvailable.toDouble() / numberOfWorkPeriodsPerCycle.toDouble()).toInt()
            val numberOfCycles = Random.nextInt(0, maxNumberOfCyclesForTest)
            val result = testedUseCase.execute(
                numberOfWorkPeriodsPerCycle,
                numberOfCycles,
                testSelectedExercises
            )
            val expectedSize = numberOfWorkPeriodsPerCycle * numberOfCycles
            assertEquals(expectedSize, result.size)
            assertTrue(expectedSize <= exercisesForSelectedTypesAvailable)
            //size is small enough to expect unicity, except for asymmetrical ones
            for (exercise in result.distinct()) {
                assertTrue(testSelectedExercises.contains(exercise.exerciseType))
                //because we add the source list again in case all the remaining exercises are asymmetrical for the last spot,
                // we can't guarantee unicity in 100% of cases. It should still be rare enough that it shouldn't matter, but
                // we don't want random failing tests so we're not asserting unicity
            }
        }

    @RepeatedTest(50)
    fun `Testing created list is correct size and exercises are only of selected types when requested number is too big`() =
        runTest {
            val testedUseCase = ComposeExercisesListForSessionUseCase(
                defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                hiitLogger = mockHiitLogger
            )
            val fullListOfExerciseTypes = ExerciseType.values().toList()
            val testNumberOfSelectedTypes = Random.nextInt(1, fullListOfExerciseTypes.size)
            val testSelectedExercises =
                fullListOfExerciseTypes.shuffled().take(testNumberOfSelectedTypes)
            val exercisesForSelectedTypesAvailable = Exercise.values().toList()
                .filter { testSelectedExercises.contains(it.exerciseType) }.size
            val numberOfWorkPeriodsPerCycle = Random.nextInt(1, 8)
            //now we want to do tests for when there too many work periods total for the available number of exercises of selected types
            val minNumberOfCyclesForTest =
                1 + (exercisesForSelectedTypesAvailable.toDouble() / numberOfWorkPeriodsPerCycle.toDouble()).toInt()
            val numberOfCycles =
                Random.nextInt(minNumberOfCyclesForTest, 2 * minNumberOfCyclesForTest)
            val result = testedUseCase.execute(
                numberOfWorkPeriodsPerCycle,
                numberOfCycles,
                testSelectedExercises
            )
            val expectedSize = numberOfWorkPeriodsPerCycle * numberOfCycles
            assertEquals(expectedSize, result.size)
            assertTrue(expectedSize > exercisesForSelectedTypesAvailable)
            //size is too big to expect unicity, we can't test the composition of the list further
            // due to the way we're looping through the source to build the list
            for (exercise in result.distinct()) {
                assertTrue(testSelectedExercises.contains(exercise.exerciseType))
            }
        }

}
