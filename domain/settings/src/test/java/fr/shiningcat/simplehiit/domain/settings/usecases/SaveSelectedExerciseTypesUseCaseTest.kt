package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
internal class SaveSelectedExerciseTypesUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()

    @ParameterizedTest(name = "{index} -> when called with {0}, should call SimpleHiitRepository with {0}")
    @MethodSource("selectedExerciseTypesArguments")
    fun `calls repo with corresponding value and returns repo success`(
        testValue: List<ExerciseTypeSelected>,
        expectedOutput: List<ExerciseType>
    ) = runTest {
        val testedUseCase =
            SaveSelectedExerciseTypesUseCase(
                simpleHiitRepository = mockSimpleHiitRepository,
                defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                simpleHiitLogger = mockHiitLogger
            )
        coEvery { mockSimpleHiitRepository.setExercisesTypesSelected(any()) } just Runs
        //
        testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.setExercisesTypesSelected(expectedOutput) }
    }

    ////////////////////////
    private companion object {

        @JvmStatic
        fun selectedExerciseTypesArguments(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    listOf(
                        ExerciseTypeSelected(ExerciseType.LUNGE, true),
                        ExerciseTypeSelected(ExerciseType.LYING, false),
                        ExerciseTypeSelected(ExerciseType.CRAB, false),
                        ExerciseTypeSelected(ExerciseType.CAT, false),
                        ExerciseTypeSelected(ExerciseType.SITTING, false),
                        ExerciseTypeSelected(ExerciseType.SQUAT, true),
                        ExerciseTypeSelected(ExerciseType.SITTING, false),
                        ExerciseTypeSelected(ExerciseType.PLANK, true)
                    ),
                    listOf(
                        ExerciseType.LUNGE,
                        ExerciseType.SQUAT,
                        ExerciseType.PLANK
                    )
                ),
                Arguments.of(
                    listOf(
                        ExerciseTypeSelected(ExerciseType.LUNGE, true),
                        ExerciseTypeSelected(ExerciseType.LYING, true),
                        ExerciseTypeSelected(ExerciseType.CRAB, true),
                        ExerciseTypeSelected(ExerciseType.CAT, true),
                        ExerciseTypeSelected(ExerciseType.SITTING, true),
                        ExerciseTypeSelected(ExerciseType.SQUAT, true),
                        ExerciseTypeSelected(ExerciseType.SITTING, true),
                        ExerciseTypeSelected(ExerciseType.PLANK, true)
                    ),
                    listOf(
                        ExerciseType.LUNGE,
                        ExerciseType.LYING,
                        ExerciseType.CRAB,
                        ExerciseType.CAT,
                        ExerciseType.SITTING,
                        ExerciseType.SQUAT,
                        ExerciseType.SITTING,
                        ExerciseType.PLANK
                    )
                ),
                Arguments.of(
                    listOf(
                        ExerciseTypeSelected(ExerciseType.LUNGE, false),
                        ExerciseTypeSelected(ExerciseType.LYING, false),
                        ExerciseTypeSelected(ExerciseType.CRAB, false),
                        ExerciseTypeSelected(ExerciseType.CAT, false),
                        ExerciseTypeSelected(ExerciseType.SITTING, false),
                        ExerciseTypeSelected(ExerciseType.SQUAT, false),
                        ExerciseTypeSelected(ExerciseType.SITTING, false),
                        ExerciseTypeSelected(ExerciseType.PLANK, false)
                    ),
                    emptyList<ExerciseType>()
                )

            )

    }

}