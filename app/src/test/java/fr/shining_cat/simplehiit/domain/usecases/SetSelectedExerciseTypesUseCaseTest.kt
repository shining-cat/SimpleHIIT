package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.utils.HiitLogger
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
internal class SetSelectedExerciseTypesUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val mockSimpleHiitLogger = mockk<HiitLogger>()
    private val testedUseCase = SetSelectedExerciseTypesUseCase(mockSimpleHiitRepository, mockHiitLogger)

    @ParameterizedTest(name = "{index} -> when called with {0}, should call SimpleHiitRepository with {0}")
    @MethodSource("selectedExerciseTypesArguments")
    fun `calls repo with corresponding value and returns repo success`(
        testValue: List<ExerciseType>
    ) = runTest {
        coEvery { mockSimpleHiitRepository.setExercisesTypesSelected(any()) } just Runs
        //
        testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.setExercisesTypesSelected(testValue) }
    }
    ////////////////////////
    private companion object {

        @JvmStatic
        fun selectedExerciseTypesArguments() =
            Stream.of(
                Arguments.of(
                    listOf(ExerciseType.LUNGE, ExerciseType.SITTING, ExerciseType.LYING)
                ),
                Arguments.of(
                    listOf(ExerciseType.SITTING, ExerciseType.PLANK, ExerciseType.SQUAT, ExerciseType.CRAB)
                ),
                Arguments.of(
                    ExerciseType.values().toList()
                )

            )

    }

}