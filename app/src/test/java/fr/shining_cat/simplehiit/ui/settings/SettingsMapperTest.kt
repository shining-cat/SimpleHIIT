package fr.shining_cat.simplehiit.ui.settings

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class SettingsMapperTest : AbstractMockkTest() {

    private val testedMapper = SettingsMapper(mockHiitLogger)

    @ParameterizedTest(name = "{index} -> should return {0}")
    @MethodSource("generalSettingsArguments")
    fun `mapping generalSettings to correct viewstate`(
        input: Output<GeneralSettings>,
        expectedOutput: SettingsViewState
    ) {
        val result = testedMapper.map(input)
        //
        assertEquals(expectedOutput, result)
    }

    private companion object {

        private val testUser1 = User(id = 123L, name = "test user 1 name", selected = true)
        private val testUser2 = User(id = 234L, name = "test user 2 name", selected = false)
        private val testUser3 = User(id = 345L, name = "test user 3 name", selected = true)
        private val testUser4 = User(id = 456L, name = "test user 4 name", selected = false)
        private val testExerciseTypeSelected1 = ExerciseTypeSelected(type = ExerciseType.CRAB, selected = false)
        private val testExerciseTypeSelected2 = ExerciseTypeSelected(type = ExerciseType.PLANK, selected = true)
        private val testExerciseTypeSelected3 = ExerciseTypeSelected(type = ExerciseType.SITTING, selected = false)
        private val testExerciseTypeSelected4 = ExerciseTypeSelected(type = ExerciseType.SQUAT, selected = true)
        private val testException = Exception("this is a test exception")

        @JvmStatic
        fun generalSettingsArguments() =
            Stream.of(
                Arguments.of(
                    Output.Success(GeneralSettings(
                        workPeriodLengthMs = 15,
                        restPeriodLengthMs = 10,
                        numberOfWorkPeriods = 6,
                        beepSoundCountDownActive = true,
                        sessionStartCountDownLengthMs = 5,
                        periodsStartCountDownLengthMs = 20,
                        users = listOf(testUser1, testUser3, testUser2, testUser4),
                        exerciseTypes = listOf(testExerciseTypeSelected1, testExerciseTypeSelected4)
                    )),
                    SettingsViewState.SettingsNominal(
                        workPeriodLengthMs = 15,
                        restPeriodLengthMs = 10,
                        numberOfWorkPeriods = 6,
                        totalCycleLengthMs = 140,
                        beepSoundCountDownActive = true,
                        sessionStartCountDownLengthMs = 5,
                        periodsStartCountDownLengthMs = 20,
                        users = listOf(testUser1, testUser3, testUser2, testUser4),
                        exerciseTypes = listOf(testExerciseTypeSelected1, testExerciseTypeSelected4)
                    )
                ),
                Arguments.of(
                    Output.Success(GeneralSettings(
                        workPeriodLengthMs = 21,
                        restPeriodLengthMs = 13,
                        numberOfWorkPeriods = 7,
                        beepSoundCountDownActive = false,
                        sessionStartCountDownLengthMs = 7,
                        periodsStartCountDownLengthMs = 34,
                        users = listOf(testUser1, testUser2),
                        exerciseTypes = listOf(testExerciseTypeSelected2, testExerciseTypeSelected3, testExerciseTypeSelected1)
                    )),
                    SettingsViewState.SettingsNominal(
                        workPeriodLengthMs = 21,
                        restPeriodLengthMs = 13,
                        numberOfWorkPeriods = 7,
                        totalCycleLengthMs = 225,
                        beepSoundCountDownActive = false,
                        sessionStartCountDownLengthMs = 7,
                        periodsStartCountDownLengthMs = 34,
                        users = listOf(testUser1, testUser2),
                        exerciseTypes = listOf(testExerciseTypeSelected2, testExerciseTypeSelected3, testExerciseTypeSelected1)
                    )
                ),
                Arguments.of(
                    Output.Error(errorCode = Constants.Errors.NO_USERS_FOUND, exception = testException),
                    SettingsViewState.SettingsError(Constants.Errors.NO_USERS_FOUND.code)
                ),
                Arguments.of(
                    Output.Error(errorCode = Constants.Errors.DATABASE_FETCH_FAILED, exception = testException),
                    SettingsViewState.SettingsError(Constants.Errors.DATABASE_FETCH_FAILED.code)
                )
            )
    }
}