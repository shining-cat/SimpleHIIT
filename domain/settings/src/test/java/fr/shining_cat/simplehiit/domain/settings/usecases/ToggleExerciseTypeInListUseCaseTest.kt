package fr.shining_cat.simplehiit.domain.settings.usecases

import fr.shining_cat.simplehiit.domain.common.models.ExerciseType
import fr.shining_cat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.settings.usecases.ToggleExerciseTypeInListUseCase
import fr.shining_cat.simplehiit.testutils.AbstractMockkTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
internal class ToggleExerciseTypeInListUseCaseTest : AbstractMockkTest() {

    @ParameterizedTest(name = "{index} -> should return {0}")
    @MethodSource("exerciseTogglingArguments")
    fun `finding average number of sessions per 7-days period`(
        startList: List<ExerciseTypeSelected>,
        exerciseToToggle: ExerciseTypeSelected,
        expectedOutput: List<ExerciseTypeSelected>
    ) = runTest {
        val testedUseCase =
            ToggleExerciseTypeInListUseCase(hiitLogger = mockHiitLogger)
        val result = testedUseCase.execute(
            currentList = startList,
            exerciseTypeToToggle = exerciseToToggle
        )
        //
        assertEquals(expectedOutput, result)
    }

    private companion object {

        @JvmStatic
        fun exerciseTogglingArguments() =
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
                    ExerciseTypeSelected(
                        ExerciseType.SQUAT,
                        true
                    ),//should be toggled to false in output list
                    listOf(
                        ExerciseTypeSelected(ExerciseType.LUNGE, true),
                        ExerciseTypeSelected(ExerciseType.LYING, false),
                        ExerciseTypeSelected(ExerciseType.CRAB, false),
                        ExerciseTypeSelected(ExerciseType.CAT, false),
                        ExerciseTypeSelected(ExerciseType.SITTING, false),
                        ExerciseTypeSelected(ExerciseType.SQUAT, false),
                        ExerciseTypeSelected(ExerciseType.SITTING, false),
                        ExerciseTypeSelected(ExerciseType.PLANK, true)
                    )
                ),
                Arguments.of(
                    listOf(
                        ExerciseTypeSelected(ExerciseType.LUNGE, true),
                        ExerciseTypeSelected(ExerciseType.LYING, false),
                        ExerciseTypeSelected(ExerciseType.CRAB, false),
                        ExerciseTypeSelected(ExerciseType.CAT, false),
                        ExerciseTypeSelected(ExerciseType.SITTING, false),
                        ExerciseTypeSelected(ExerciseType.SQUAT, false),
                        ExerciseTypeSelected(ExerciseType.SITTING, false),
                        ExerciseTypeSelected(ExerciseType.PLANK, true)
                    ),
                    ExerciseTypeSelected(
                        ExerciseType.SQUAT,
                        false
                    ),//should be toggled to true in output list
                    listOf(
                        ExerciseTypeSelected(ExerciseType.LUNGE, true),
                        ExerciseTypeSelected(ExerciseType.LYING, false),
                        ExerciseTypeSelected(ExerciseType.CRAB, false),
                        ExerciseTypeSelected(ExerciseType.CAT, false),
                        ExerciseTypeSelected(ExerciseType.SITTING, false),
                        ExerciseTypeSelected(ExerciseType.SQUAT, true),
                        ExerciseTypeSelected(ExerciseType.SITTING, false),
                        ExerciseTypeSelected(ExerciseType.PLANK, true)
                    )
                ),
                Arguments.of(
                    listOf(
                        ExerciseTypeSelected(ExerciseType.LUNGE, true)
                    ),
                    ExerciseTypeSelected(
                        ExerciseType.LUNGE,
                        true
                    ),//should be toggled to false in output list
                    listOf(
                        ExerciseTypeSelected(ExerciseType.LUNGE, false)
                    )
                ),
                Arguments.of(
                    listOf(
                        ExerciseTypeSelected(ExerciseType.LUNGE, false)
                    ),
                    ExerciseTypeSelected(
                        ExerciseType.LUNGE,
                        false
                    ),//should be toggled to true in output list
                    listOf(
                        ExerciseTypeSelected(ExerciseType.LUNGE, true)
                    )
                ),
                Arguments.of(
                    listOf(
                        ExerciseTypeSelected(ExerciseType.LUNGE, true)
                    ),
                    ExerciseTypeSelected(ExerciseType.SQUAT, true),
                    listOf(
                        ExerciseTypeSelected(
                            ExerciseType.LUNGE,
                            true
                        )//exercise to toggle is not in list => list is not changed
                    )
                ),
            )
    }
}