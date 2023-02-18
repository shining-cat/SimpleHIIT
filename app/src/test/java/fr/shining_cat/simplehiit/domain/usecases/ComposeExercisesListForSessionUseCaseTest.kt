package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.models.Exercise
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import java.util.*
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
internal class ComposeExercisesListForSessionUseCaseTest : AbstractMockkTest() {

    val testedUseCase = ComposeExercisesListForSessionUseCase(mockHiitLogger)

    @RepeatedTest(50)
    fun `Testing created list is correct size and exercises are only present once`() = runTest {
        val numberOfWorkPeriodsPerCycle = Random.nextInt(1, 8)
        val numberOfCycles = Random.nextInt(1, 5)
        val result = testedUseCase.execute(numberOfWorkPeriodsPerCycle, numberOfCycles)
        val expectedSize = numberOfWorkPeriodsPerCycle * numberOfCycles
        assertEquals(expectedSize, result.size)
        val totalAvailableExercises = Exercise.values().size
        assertTrue(expectedSize < totalAvailableExercises)
        //size is small enough to expect unicity, except for asymmetrical ones
        for(exercise in result.distinct()){
            var frequency = Collections.frequency(result, exercise)
            if(exercise.asymmetrical){
                assertEquals(2, frequency)
            } else{
                assertEquals(1, frequency)
            }
        }
    }

    @RepeatedTest(10)
    fun `Testing created list is correct size when requested is too big`() = runTest {
        val numberOfWorkPeriodsPerCycle = Random.nextInt(10, 20)
        val numberOfCycles = Random.nextInt(6, 20)
        val result = testedUseCase.execute(numberOfWorkPeriodsPerCycle, numberOfCycles)
        val expectedSize = numberOfWorkPeriodsPerCycle * numberOfCycles
        assertEquals(expectedSize, result.size)
        val totalAvailableExercises = Exercise.values().size
        assertTrue(expectedSize > totalAvailableExercises)
        //size is too big to expect unicity, we can't test the composition of the list further
    }

}
