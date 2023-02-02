package fr.shining_cat.simplehiit.data.local.preferences

import android.content.SharedPreferences
import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.DefaultValues.BEEP_SOUND_ACTIVE_DEFAULT
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.DefaultValues.NUMBER_CUMULATED_CYCLES_DEFAULT
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.DefaultValues.NUMBER_WORK_PERIODS_DEFAULT
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.DefaultValues.PERIOD_COUNTDOWN_LENGTH_SECONDS_DEFAULT
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.DefaultValues.REST_PERIOD_LENGTH_SECONDS_DEFAULT
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.DefaultValues.SESSION_COUNTDOWN_LENGTH_SECONDS_DEFAULT
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.DefaultValues.WORK_PERIOD_LENGTH_SECONDS_DEFAULT
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.BEEP_SOUND_ACTIVE
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.EXERCISE_TYPES_SELECTED
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.NUMBER_CUMULATED_CYCLES
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.NUMBER_WORK_PERIODS
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.PERIOD_COUNTDOWN_LENGTH_SECONDS
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.REST_PERIOD_LENGTH_SECONDS
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.SESSION_COUNTDOWN_LENGTH_SECONDS
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.WORK_PERIOD_LENGTH_SECONDS
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.domain.models.ExerciseType.*
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

@ExperimentalCoroutinesApi
internal class SimpleHiitPreferencesImplTest : AbstractMockkTest() {

    private val mockSharedPreferences = mockk<SharedPreferences>()
    private val mockSharedPrefsEditor = mockk<SharedPreferences.Editor>()
    private val simpleHiitPreferences = SimpleHiitPreferencesImpl(
        sharedPreferences = mockSharedPreferences,
        hiitLogger = mockHiitLogger
    )

    @BeforeEach
    override fun setupBeforeEach() {
        super.setupBeforeEach()
        coEvery { mockSharedPreferences.edit() } returns mockSharedPrefsEditor
        coEvery { mockSharedPrefsEditor.clear() } returns mockSharedPrefsEditor
        coEvery { mockSharedPrefsEditor.apply() } answers {}
        coEvery { mockSharedPrefsEditor.commit() } answers { true }
    }

    @Test
    fun `clearAll clears sharedprefs`() = runTest {
        simpleHiitPreferences.clearAll()
        //
        coVerify(exactly = 1) { mockSharedPreferences.edit() }
        coVerify(exactly = 1) { mockSharedPrefsEditor.clear() }
    }

    @ParameterizedTest(name = "{index} -> when called with {0} should set sharedPrefs WORK_PERIOD_LENGTH_SECONDS to {0}")
    @ValueSource(ints = [-1, 0, 1, 5, 10, 10000000])
    fun `setWorkPeriodLength calls sharedPrefs with same value`(
        testValue: Int
    ) = runTest {
        coEvery { mockSharedPrefsEditor.putInt(any(), any()) } returns mockSharedPrefsEditor
        //
        simpleHiitPreferences.setWorkPeriodLength(testValue)
        //
        coVerify(exactly = 1) {
            mockSharedPrefsEditor.putInt(
                WORK_PERIOD_LENGTH_SECONDS,
                testValue
            )
        }
    }

    @ParameterizedTest(name = "{index} -> when called should get sharedPrefs WORK_PERIOD_LENGTH_SECONDS and it returns {0} should return {0}")
    @ValueSource(ints = [-1, 0, 1, 5, 10, 10000000])
    fun `getWorkPeriodLength calls sharedPrefs and return value`(
        testValue: Int
    ) = runTest {
        val defaultSlot = slot<Int>()
        coEvery {
            mockSharedPreferences.getInt(
                WORK_PERIOD_LENGTH_SECONDS,
                capture(defaultSlot)
            )
        } returns testValue
        //
        val result = simpleHiitPreferences.getWorkPeriodLengthSeconds()
        //
        coVerify(exactly = 1) { mockSharedPreferences.getInt(WORK_PERIOD_LENGTH_SECONDS, any()) }
        assertEquals(WORK_PERIOD_LENGTH_SECONDS_DEFAULT, defaultSlot.captured)
        assertEquals(testValue, result)
    }

    @ParameterizedTest(name = "{index} -> when called with {0} should set sharedPrefs WORK_PERIOD_LENGTH_SECONDS to {0}")
    @ValueSource(ints = [-1, 0, 1, 5, 10, 10000000])
    fun `setRestPeriodLength calls sharedPrefs with same value`(
        testValue: Int
    ) = runTest {
        coEvery { mockSharedPrefsEditor.putInt(any(), any()) } returns mockSharedPrefsEditor
        //
        simpleHiitPreferences.setRestPeriodLength(testValue)
        //
        coVerify(exactly = 1) {
            mockSharedPrefsEditor.putInt(
                REST_PERIOD_LENGTH_SECONDS,
                testValue
            )
        }
    }

    @ParameterizedTest(name = "{index} -> when called should get sharedPrefs WORK_PERIOD_LENGTH_SECONDS and it returns {0} should return {0}")
    @ValueSource(ints = [-1, 0, 1, 5, 10, 10000000])
    fun `getRestPeriodLength calls sharedPrefs and return value`(
        testValue: Int
    ) = runTest {
        val defaultSlot = slot<Int>()
        coEvery {
            mockSharedPreferences.getInt(
                REST_PERIOD_LENGTH_SECONDS,
                capture(defaultSlot)
            )
        } returns testValue
        //
        val result = simpleHiitPreferences.getRestPeriodLengthSeconds()
        //
        coVerify(exactly = 1) { mockSharedPreferences.getInt(REST_PERIOD_LENGTH_SECONDS, any()) }
        assertEquals(REST_PERIOD_LENGTH_SECONDS_DEFAULT, defaultSlot.captured)
        assertEquals(testValue, result)
    }

    @ParameterizedTest(name = "{index} -> when called with {0} should set sharedPrefs WORK_PERIOD_LENGTH_SECONDS to {0}")
    @ValueSource(ints = [-1, 0, 1, 5, 10, 10000000])
    fun `setNumberOfWorkPeriods calls sharedPrefs with same value`(
        testValue: Int
    ) = runTest {
        coEvery { mockSharedPrefsEditor.putInt(any(), any()) } returns mockSharedPrefsEditor
        //
        simpleHiitPreferences.setNumberOfWorkPeriods(testValue)
        //
        coVerify(exactly = 1) { mockSharedPrefsEditor.putInt(NUMBER_WORK_PERIODS, testValue) }
    }

    @ParameterizedTest(name = "{index} -> when called should get sharedPrefs WORK_PERIOD_LENGTH_SECONDS and it returns {0} should return {0}")
    @ValueSource(ints = [-1, 0, 1, 5, 10, 10000000])
    fun `getNumberOfWorkPeriods calls sharedPrefs and return value`(
        testValue: Int
    ) = runTest {
        val defaultSlot = slot<Int>()
        coEvery {
            mockSharedPreferences.getInt(
                NUMBER_WORK_PERIODS,
                capture(defaultSlot)
            )
        } returns testValue
        //
        val result = simpleHiitPreferences.getNumberOfWorkPeriods()
        //
        coVerify(exactly = 1) { mockSharedPreferences.getInt(NUMBER_WORK_PERIODS, any()) }
        assertEquals(NUMBER_WORK_PERIODS_DEFAULT, defaultSlot.captured)
        assertEquals(testValue, result)
    }

    @ParameterizedTest(name = "{index} -> when called with {0}, calls SharedPrefs with {0}")
    @ValueSource(booleans = [true, false])
    fun `calling setBeepSound calls SharedPrefs with true`(
        testValue: Boolean
    ) = runTest {
        coEvery { mockSharedPrefsEditor.putBoolean(any(), any()) } returns mockSharedPrefsEditor
        //
        simpleHiitPreferences.setBeepSound(testValue)
        //
        coVerify(exactly = 1) { mockSharedPrefsEditor.putBoolean(BEEP_SOUND_ACTIVE, testValue) }
    }

    @ParameterizedTest(name = "{index} -> when SharedPrefs return {0}, should return {0}")
    @ValueSource(booleans = [true, false])
    fun `calling getBeepSound calls and return value from SharedPrefs, defaulting to BEEP_SOUND_ACTIVE_DEFAULT`(
        expected: Boolean
    ) = runTest {
        val defaultSlot = slot<Boolean>()
        coEvery {
            mockSharedPreferences.getBoolean(
                BEEP_SOUND_ACTIVE,
                capture(defaultSlot)
            )
        } returns expected
        //
        val result = simpleHiitPreferences.getBeepSoundActive()
        //
        coVerify(exactly = 1) { mockSharedPreferences.getBoolean(BEEP_SOUND_ACTIVE, any()) }
        assertEquals(BEEP_SOUND_ACTIVE_DEFAULT, defaultSlot.captured)
        assertEquals(expected, result)
    }

    @ParameterizedTest(name = "{index} -> when called with {0} should set sharedPrefs WORK_PERIOD_LENGTH_SECONDS to {0}")
    @ValueSource(ints = [-1, 0, 1, 5, 10, 10000000])
    fun `setSessionStartCountdown calls sharedPrefs with same value`(
        testValue: Int
    ) = runTest {
        coEvery { mockSharedPrefsEditor.putInt(any(), any()) } returns mockSharedPrefsEditor
        //
        simpleHiitPreferences.setSessionStartCountdown(testValue)
        //
        coVerify(exactly = 1) {
            mockSharedPrefsEditor.putInt(
                SESSION_COUNTDOWN_LENGTH_SECONDS,
                testValue
            )
        }
    }

    @ParameterizedTest(name = "{index} -> when called should get sharedPrefs WORK_PERIOD_LENGTH_SECONDS and it returns {0} should return {0}")
    @ValueSource(ints = [-1, 0, 1, 5, 10, 10000000])
    fun `getSessionStartCountdown calls sharedPrefs and return value`(
        testValue: Int
    ) = runTest {
        val defaultSlot = slot<Int>()
        coEvery {
            mockSharedPreferences.getInt(
                SESSION_COUNTDOWN_LENGTH_SECONDS,
                capture(defaultSlot)
            )
        } returns testValue
        //
        val result = simpleHiitPreferences.getSessionStartCountdown()
        //
        coVerify(exactly = 1) {
            mockSharedPreferences.getInt(
                SESSION_COUNTDOWN_LENGTH_SECONDS,
                any()
            )
        }
        assertEquals(SESSION_COUNTDOWN_LENGTH_SECONDS_DEFAULT, defaultSlot.captured)
        assertEquals(testValue, result)
    }

    @ParameterizedTest(name = "{index} -> when called with {0} should set sharedPrefs WORK_PERIOD_LENGTH_SECONDS to {0}")
    @ValueSource(ints = [-1, 0, 1, 5, 10, 10000000])
    fun `setPeriodStartCountdown calls sharedPrefs with same value`(
        testValue: Int
    ) = runTest {
        coEvery { mockSharedPrefsEditor.putInt(any(), any()) } returns mockSharedPrefsEditor
        //
        simpleHiitPreferences.setPeriodStartCountdown(testValue)
        //
        coVerify(exactly = 1) {
            mockSharedPrefsEditor.putInt(
                PERIOD_COUNTDOWN_LENGTH_SECONDS,
                testValue
            )
        }
    }

    @ParameterizedTest(name = "{index} -> when called should get sharedPrefs WORK_PERIOD_LENGTH_SECONDS and it returns {0} should return {0}")
    @ValueSource(ints = [-1, 0, 1, 5, 10, 10000000])
    fun `getPeriodStartCountdown calls sharedPrefs and return value`(
        testValue: Int
    ) = runTest {
        val defaultSlot = slot<Int>()
        coEvery {
            mockSharedPreferences.getInt(
                PERIOD_COUNTDOWN_LENGTH_SECONDS,
                capture(defaultSlot)
            )
        } returns testValue
        //
        val result = simpleHiitPreferences.getPeriodStartCountdown()
        //
        coVerify(exactly = 1) {
            mockSharedPreferences.getInt(
                PERIOD_COUNTDOWN_LENGTH_SECONDS,
                any()
            )
        }
        assertEquals(PERIOD_COUNTDOWN_LENGTH_SECONDS_DEFAULT, defaultSlot.captured)
        assertEquals(testValue, result)
    }

    @ParameterizedTest(name = "{index} -> when called with {0} should set sharedPrefs WORK_PERIOD_LENGTH_SECONDS to {0}")
    @ValueSource(ints = [-1, 0, 1, 5, 10, 10000000])
    fun `setNumberOfCumulatedCycles calls sharedPrefs with same value`(
        testValue: Int
    ) = runTest {
        coEvery { mockSharedPrefsEditor.putInt(any(), any()) } returns mockSharedPrefsEditor
        //
        simpleHiitPreferences.setNumberOfCumulatedCycles(testValue)
        //
        coVerify(exactly = 1) {
            mockSharedPrefsEditor.putInt(
                NUMBER_CUMULATED_CYCLES,
                testValue
            )
        }
    }

    @ParameterizedTest(name = "{index} -> when called should get sharedPrefs WORK_PERIOD_LENGTH_SECONDS and it returns {0} should return {0}")
    @ValueSource(ints = [-1, 0, 1, 5, 10, 10000000])
    fun `getNumberOfCumulatedCycles calls sharedPrefs and return value`(
        testValue: Int
    ) = runTest {
        val defaultSlot = slot<Int>()
        coEvery {
            mockSharedPreferences.getInt(
                NUMBER_CUMULATED_CYCLES,
                capture(defaultSlot)
            )
        } returns testValue
        //
        val result = simpleHiitPreferences.getNumberOfCumulatedCycles()
        //
        coVerify(exactly = 1) {
            mockSharedPreferences.getInt(
                NUMBER_CUMULATED_CYCLES,
                any()
            )
        }
        assertEquals(NUMBER_CUMULATED_CYCLES_DEFAULT, defaultSlot.captured)
        assertEquals(testValue, result)
    }

    @ParameterizedTest(name = "{index} -> when called with {0} set sharedPrefs SELECTED_USERS_IDS to {1}")
    @MethodSource("setExercisesTypesSelectedArguments")
    fun `setExercisesTypesSelected calls SharedPrefs with correct list`(
        inputList: List<ExerciseType>,
        expectedSharedPrefsCallList: Set<String>
    ) = runTest {
        val setSlot = slot<Set<String>>()
        coEvery {
            mockSharedPrefsEditor.putStringSet(
                any(),
                capture(setSlot)
            )
        } returns mockSharedPrefsEditor
        //
        simpleHiitPreferences.setExercisesTypesSelected(inputList)
        //
        coVerify(exactly = 1) { mockSharedPrefsEditor.putStringSet(EXERCISE_TYPES_SELECTED, any()) }
        assertEquals(expectedSharedPrefsCallList, setSlot.captured)
    }

    @ParameterizedTest(name = "{index} -> when SharedPrefs returns {0} should return empty list")
    @MethodSource("getExercisesTypesSelectedNothingArguments")
    fun `getExercisesTypesSelected no values found`(
        testValue: Set<String>?
    ) = runTest {
        val defaultSlot = slot<Set<String>>()
        coEvery {
            mockSharedPreferences.getStringSet(
                EXERCISE_TYPES_SELECTED,
                capture(defaultSlot)
            )
        } returns testValue
        //
        val result = simpleHiitPreferences.getExercisesTypesSelected()
        //
        coVerify(exactly = 1) {
            mockSharedPreferences.getStringSet(
                EXERCISE_TYPES_SELECTED,
                any()
            )
        }
        assertEquals(emptySet<String>(), defaultSlot.captured)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getExercisesTypesSelected error case failed to parse to ExerciseType`() = runTest {
        val sharedPrefCorruptedSet = setOf("CAT", "CRABcorrupted-123", "LUNGE", "LYING")
        val defaultSlot = slot<Set<String>>()
        coEvery {
            mockSharedPreferences.getStringSet(
                EXERCISE_TYPES_SELECTED,
                capture(defaultSlot)
            )
        } returns sharedPrefCorruptedSet
        val setSlot = slot<Set<String>>()
        coEvery {
            mockSharedPrefsEditor.putStringSet(
                any(),
                capture(setSlot)
            )
        } returns mockSharedPrefsEditor
        //
        val result = simpleHiitPreferences.getExercisesTypesSelected()
        //
        //verify first call
        coVerify(exactly = 1) {
            mockSharedPreferences.getStringSet(
                EXERCISE_TYPES_SELECTED,
                any()
            )
        }
        assertEquals(emptySet<String>(), defaultSlot.captured)
        //verify exception logging
        val exceptionSlot = slot<Exception>()
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "getExercisesTypesSelected corruption found, resetting stored list to complete list", capture(exceptionSlot)) }
        assertTrue(exceptionSlot.captured is IllegalArgumentException)
        //verify delete call
        coVerify(exactly = 1) { mockSharedPrefsEditor.putStringSet(EXERCISE_TYPES_SELECTED, any()) }
        val expectedCompleteSet = ExerciseType.values().map { it.name }.toSet()
        assertEquals(expectedCompleteSet, setSlot.captured)
        //verify final empty result
        val expectedCompleteList = ExerciseType.values().toList()
        assertEquals(expectedCompleteList, result)
    }

    @ParameterizedTest(name = "{index} -> when SharedPrefs returns {0} should return {1}")
    @MethodSource("getExercisesTypesSelectedArguments")
    fun `getExercisesTypesSelected happy case`(
        testValue: Set<String>,
        expectedResult:List<Long>
    ) = runTest {
        val defaultSlot = slot<Set<String>>()
        coEvery {
            mockSharedPreferences.getStringSet(
                EXERCISE_TYPES_SELECTED,
                capture(defaultSlot)
            )
        } returns testValue
        //
        val result = simpleHiitPreferences.getExercisesTypesSelected()
        //
        coVerify(exactly = 1) {
            mockSharedPreferences.getStringSet(
                EXERCISE_TYPES_SELECTED,
                any()
            )
        }
        assertEquals(emptySet<String>(), defaultSlot.captured)
        assertEquals(expectedResult, result)
    }

    private companion object {

        @JvmStatic
        fun setExercisesTypesSelectedArguments() =
            Stream.of(
                Arguments.of(
                    emptyList<ExerciseType>(),
                    emptySet<String>()
                ),
                Arguments.of(
                    listOf(LUNGE),
                    setOf("LUNGE")
                ),
                Arguments.of(
                    listOf(CAT, LUNGE, PLANK, SQUAT),
                    setOf("CAT", "LUNGE", "PLANK", "SQUAT")
                ),
                Arguments.of(
                    listOf(CAT, CRAB, LUNGE, LYING, PLANK, SITTING, SQUAT, STANDING),
                    setOf("CAT", "CRAB", "LUNGE", "LYING", "PLANK", "SITTING", "SQUAT", "STANDING")
                )
            )

        @JvmStatic
        fun getExercisesTypesSelectedNothingArguments() =
            Stream.of(
                Arguments.of(emptySet<String>()),
                Arguments.of(null)
            )

        @JvmStatic
        fun getExercisesTypesSelectedArguments() =
            Stream.of(
                Arguments.of(
                    setOf("LUNGE"),
                    listOf(LUNGE)
                ),
                Arguments.of(
                    setOf("CAT", "LUNGE", "PLANK", "SQUAT"),
                    listOf(CAT, LUNGE, PLANK, SQUAT)
                ),
                Arguments.of(
                    setOf("CAT", "CRAB", "LUNGE", "LYING", "PLANK", "SITTING", "SQUAT", "STANDING"),
                    listOf(CAT, CRAB, LUNGE, LYING, PLANK, SITTING, SQUAT, STANDING)
                )
            )
    }

}