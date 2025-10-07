package fr.shiningcat.simplehiit.domain.home.usecases

import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.LaunchSessionWarning
import fr.shiningcat.simplehiit.domain.common.models.SessionSettings
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class DetectSessionWarningUseCaseTest : AbstractMockkTest() {
    private val testedUseCase = DetectSessionWarningUseCase()

    @ParameterizedTest(name = "{index} -> {0} should return {1}")
    @MethodSource("warningDetectionArguments")
    fun `detecting session warnings`(
        testName: String,
        sessionSettings: SessionSettings,
        expectedWarning: LaunchSessionWarning?,
    ) {
        val result = testedUseCase.execute(sessionSettings)
        assertEquals(expectedWarning, result)
    }

    private companion object {
        private val userSelected = User(id = 1L, name = "Selected User", selected = true)
        private val userNotSelected = User(id = 2L, name = "Not Selected User", selected = false)

        // Exercise type counts (from Exercise enum):
        // STANDING: 7 symmetrical + 1 asymmetrical = 9 exercises
        // SQUAT: 7 symmetrical + 1 asymmetrical = 9 exercises
        // CAT: 3 symmetrical + 2 asymmetrical = 7 exercises
        // PLANK: 6 symmetrical + 2 asymmetrical = 10 exercises
        // SITTING: 6 symmetrical + 0 asymmetrical = 6 exercises
        // CRAB: 4 symmetrical + 1 asymmetrical = 6 exercises
        // LUNGE: 8 symmetrical + 7 asymmetrical = 22 exercises
        // LYING: 5 symmetrical + 1 asymmetrical = 7 exercises

        @JvmStatic
        fun warningDetectionArguments(): Stream<Arguments> =
            Stream.of(
                // No warning - happy path
                Arguments.of(
                    "No warning when users selected and session length matches available exercises",
                    SessionSettings(
                        numberCumulatedCycles = 2,
                        workPeriodLengthMs = 20000L,
                        restPeriodLengthMs = 10000L,
                        numberOfWorkPeriods = 4, // 4 * 2 = 8 exercises needed
                        cycleLengthMs = 240000L,
                        beepSoundCountDownActive = true,
                        sessionStartCountDownLengthMs = 5000L,
                        periodsStartCountDownLengthMs = 3000L,
                        users = listOf(userSelected),
                        exerciseTypes =
                            listOf(
                                ExerciseTypeSelected(ExerciseType.STANDING, true), // 9 exercises
                                ExerciseTypeSelected(ExerciseType.SQUAT, false),
                            ),
                    ),
                    null,
                ),
                // NO_USER_SELECTED warning
                Arguments.of(
                    "NO_USER_SELECTED when no users are selected",
                    SessionSettings(
                        numberCumulatedCycles = 3,
                        workPeriodLengthMs = 20000L,
                        restPeriodLengthMs = 10000L,
                        numberOfWorkPeriods = 5,
                        cycleLengthMs = 450000L,
                        beepSoundCountDownActive = false,
                        sessionStartCountDownLengthMs = 0L,
                        periodsStartCountDownLengthMs = 0L,
                        users = listOf(userNotSelected),
                        exerciseTypes =
                            listOf(
                                ExerciseTypeSelected(ExerciseType.LUNGE, true),
                                ExerciseTypeSelected(ExerciseType.CAT, true),
                            ),
                    ),
                    LaunchSessionWarning.NO_USER_SELECTED,
                ),
                Arguments.of(
                    "NO_USER_SELECTED when users list is empty",
                    SessionSettings(
                        numberCumulatedCycles = 1,
                        workPeriodLengthMs = 30000L,
                        restPeriodLengthMs = 15000L,
                        numberOfWorkPeriods = 3,
                        cycleLengthMs = 135000L,
                        beepSoundCountDownActive = true,
                        sessionStartCountDownLengthMs = 5000L,
                        periodsStartCountDownLengthMs = 3000L,
                        users = emptyList(),
                        exerciseTypes =
                            listOf(
                                ExerciseTypeSelected(ExerciseType.PLANK, true),
                            ),
                    ),
                    LaunchSessionWarning.NO_USER_SELECTED,
                ),
                // SKIPPED_EXERCISE_TYPES warning
                Arguments.of(
                    "SKIPPED_EXERCISE_TYPES when session too short for all selected types",
                    SessionSettings(
                        numberCumulatedCycles = 1,
                        workPeriodLengthMs = 20000L,
                        restPeriodLengthMs = 10000L,
                        numberOfWorkPeriods = 2, // Only 2 exercises needed
                        cycleLengthMs = 60000L,
                        beepSoundCountDownActive = true,
                        sessionStartCountDownLengthMs = 5000L,
                        periodsStartCountDownLengthMs = 3000L,
                        users = listOf(userSelected),
                        exerciseTypes =
                            listOf(
                                ExerciseTypeSelected(ExerciseType.STANDING, true),
                                ExerciseTypeSelected(ExerciseType.SQUAT, true),
                                ExerciseTypeSelected(ExerciseType.CAT, true), // 3 types selected
                                ExerciseTypeSelected(ExerciseType.PLANK, false),
                            ),
                    ),
                    LaunchSessionWarning.SKIPPED_EXERCISE_TYPES,
                ),
                Arguments.of(
                    "SKIPPED_EXERCISE_TYPES when only 1 exercise needed but multiple types selected",
                    SessionSettings(
                        numberCumulatedCycles = 1,
                        workPeriodLengthMs = 30000L,
                        restPeriodLengthMs = 10000L,
                        numberOfWorkPeriods = 1, // Only 1 exercise needed
                        cycleLengthMs = 40000L,
                        beepSoundCountDownActive = false,
                        sessionStartCountDownLengthMs = 0L,
                        periodsStartCountDownLengthMs = 0L,
                        users = listOf(userSelected, userNotSelected),
                        exerciseTypes =
                            listOf(
                                ExerciseTypeSelected(ExerciseType.LUNGE, true),
                                ExerciseTypeSelected(ExerciseType.LYING, true), // 2 types selected
                                ExerciseTypeSelected(ExerciseType.CRAB, false),
                            ),
                    ),
                    LaunchSessionWarning.SKIPPED_EXERCISE_TYPES,
                ),
                // DUPLICATED_EXERCISES warning
                Arguments.of(
                    "DUPLICATED_EXERCISES when session too long for available exercises",
                    SessionSettings(
                        numberCumulatedCycles = 5,
                        workPeriodLengthMs = 20000L,
                        restPeriodLengthMs = 10000L,
                        numberOfWorkPeriods = 4, // 4 * 5 = 20 exercises needed
                        cycleLengthMs = 600000L,
                        beepSoundCountDownActive = true,
                        sessionStartCountDownLengthMs = 5000L,
                        periodsStartCountDownLengthMs = 3000L,
                        users = listOf(userSelected),
                        exerciseTypes =
                            listOf(
                                ExerciseTypeSelected(ExerciseType.SITTING, true), // Only 6 exercises available
                                ExerciseTypeSelected(ExerciseType.STANDING, false),
                            ),
                    ),
                    LaunchSessionWarning.DUPLICATED_EXERCISES,
                ),
                Arguments.of(
                    "DUPLICATED_EXERCISES when needing more than combined available exercises",
                    SessionSettings(
                        numberCumulatedCycles = 10,
                        workPeriodLengthMs = 30000L,
                        restPeriodLengthMs = 15000L,
                        numberOfWorkPeriods = 5, // 5 * 10 = 50 exercises needed
                        cycleLengthMs = 2250000L,
                        beepSoundCountDownActive = false,
                        sessionStartCountDownLengthMs = 0L,
                        periodsStartCountDownLengthMs = 0L,
                        users = listOf(userSelected),
                        exerciseTypes =
                            listOf(
                                ExerciseTypeSelected(ExerciseType.CAT, true), // 7 exercises
                                ExerciseTypeSelected(ExerciseType.CRAB, true), // 6 exercises
                                // Total: 13 exercises available, but 50 needed
                                ExerciseTypeSelected(ExerciseType.LUNGE, false),
                            ),
                    ),
                    LaunchSessionWarning.DUPLICATED_EXERCISES,
                ),
                // Priority verification - NO_USER_SELECTED takes precedence
                Arguments.of(
                    "NO_USER_SELECTED takes precedence over SKIPPED_EXERCISE_TYPES",
                    SessionSettings(
                        numberCumulatedCycles = 1,
                        workPeriodLengthMs = 20000L,
                        restPeriodLengthMs = 10000L,
                        numberOfWorkPeriods = 1, // Would trigger SKIPPED_EXERCISE_TYPES
                        cycleLengthMs = 30000L,
                        beepSoundCountDownActive = true,
                        sessionStartCountDownLengthMs = 5000L,
                        periodsStartCountDownLengthMs = 3000L,
                        users = listOf(userNotSelected), // But no user selected
                        exerciseTypes =
                            listOf(
                                ExerciseTypeSelected(ExerciseType.STANDING, true),
                                ExerciseTypeSelected(ExerciseType.SQUAT, true),
                                ExerciseTypeSelected(ExerciseType.CAT, true),
                            ),
                    ),
                    LaunchSessionWarning.NO_USER_SELECTED,
                ),
                Arguments.of(
                    "NO_USER_SELECTED takes precedence over DUPLICATED_EXERCISES",
                    SessionSettings(
                        numberCumulatedCycles = 10,
                        workPeriodLengthMs = 20000L,
                        restPeriodLengthMs = 10000L,
                        numberOfWorkPeriods = 10, // Would trigger DUPLICATED_EXERCISES
                        cycleLengthMs = 3000000L,
                        beepSoundCountDownActive = false,
                        sessionStartCountDownLengthMs = 0L,
                        periodsStartCountDownLengthMs = 0L,
                        users = emptyList(), // But no user selected
                        exerciseTypes =
                            listOf(
                                ExerciseTypeSelected(ExerciseType.SITTING, true), // Only 6 exercises
                            ),
                    ),
                    LaunchSessionWarning.NO_USER_SELECTED,
                ),
                // Edge case - exactly matching available exercises
                Arguments.of(
                    "No warning when exercises needed exactly matches available",
                    SessionSettings(
                        numberCumulatedCycles = 1,
                        workPeriodLengthMs = 20000L,
                        restPeriodLengthMs = 10000L,
                        numberOfWorkPeriods = 6, // Exactly 6 exercises needed
                        cycleLengthMs = 180000L,
                        beepSoundCountDownActive = true,
                        sessionStartCountDownLengthMs = 5000L,
                        periodsStartCountDownLengthMs = 3000L,
                        users = listOf(userSelected),
                        exerciseTypes =
                            listOf(
                                ExerciseTypeSelected(ExerciseType.SITTING, true), // Exactly 6 exercises available
                                ExerciseTypeSelected(ExerciseType.LUNGE, false),
                            ),
                    ),
                    null,
                ),
            )
    }
}
