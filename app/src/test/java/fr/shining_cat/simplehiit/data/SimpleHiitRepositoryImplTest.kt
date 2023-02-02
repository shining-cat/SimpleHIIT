package fr.shining_cat.simplehiit.data

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.data.local.database.dao.SessionsDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences
import fr.shining_cat.simplehiit.data.mappers.SessionMapper
import fr.shining_cat.simplehiit.data.mappers.UserMapper
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.domain.models.ExerciseType.*
import fr.shining_cat.simplehiit.domain.models.Session
import fr.shining_cat.simplehiit.domain.models.User
import io.mockk.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
internal class SimpleHiitRepositoryImplTest : AbstractMockkTest() {

    private val mockUsersDao = mockk<UsersDao>()
    private val mockSessionsDao = mockk<SessionsDao>()
    private val mockUserMapper = mockk<UserMapper>()
    private val mockSessionMapper = mockk<SessionMapper>()
    private val mockSimpleHiitPreferences = mockk<SimpleHiitPreferences>()

    private val simpleHiitRepository = SimpleHiitRepositoryImpl(
        usersDao = mockUsersDao,
        sessionsDao = mockSessionsDao,
        userMapper = mockUserMapper,
        sessionMapper = mockSessionMapper,
        hiitPreferences = mockSimpleHiitPreferences,
        hiitLogger = mockHiitLogger
    )

    @Test
    fun `setWorkPeriodLength calls hiitPreferences setWorkPeriodLength with correct value `() = runTest {
        val testValue = 123
        coEvery { mockSimpleHiitPreferences.setWorkPeriodLength(any()) } just Runs
        //
        simpleHiitRepository.setWorkPeriodLength(testValue)
        //
        coVerify (exactly = 1){ mockSimpleHiitPreferences.setWorkPeriodLength(testValue) }
    }

    @Test
    fun `getWorkPeriodLength calls hiitPreferences getWorkPeriodLength and return correct value `() = runTest {
        val testValue = 123
        coEvery { mockSimpleHiitPreferences.getWorkPeriodLengthSeconds() } returns testValue
        //
        val actual = simpleHiitRepository.getWorkPeriodLengthSeconds()
        //
        coVerify (exactly = 1){ mockSimpleHiitPreferences.getWorkPeriodLengthSeconds() }
        assertEquals(testValue, actual)
    }

///////////////////

    @Test
    fun `setRestPeriodLength calls hiitPreferences setRestPeriodLength with correct value `() = runTest {
        val testValue = 123
        coEvery { mockSimpleHiitPreferences.setRestPeriodLength(any()) } just Runs
        //
        simpleHiitRepository.setRestPeriodLength(testValue)
        //
        coVerify (exactly = 1){ mockSimpleHiitPreferences.setRestPeriodLength(testValue) }
    }

    @Test
    fun `getRestPeriodLengthSeconds calls hiitPreferences getRestPeriodLengthSeconds and return correct value `() = runTest {
        val testValue = 123
        coEvery { mockSimpleHiitPreferences.getRestPeriodLengthSeconds() } returns testValue
        //
        val actual = simpleHiitRepository.getRestPeriodLengthSeconds()
        //
        coVerify (exactly = 1){ mockSimpleHiitPreferences.getRestPeriodLengthSeconds() }
        assertEquals(testValue, actual)
    }

///////////////////

    @Test
    fun `setNumberOfWorkPeriods calls hiitPreferences setNumberOfWorkPeriods with correct value `() = runTest {
        val testValue = 123
        coEvery { mockSimpleHiitPreferences.setNumberOfWorkPeriods(any()) } just Runs
        //
        simpleHiitRepository.setNumberOfWorkPeriods(testValue)
        //
        coVerify (exactly = 1){ mockSimpleHiitPreferences.setNumberOfWorkPeriods(testValue) }
    }

    @Test
    fun `getNumberOfWorkPeriods calls hiitPreferences getNumberOfWorkPeriods and return correct value `() = runTest {
        val testValue = 123
        coEvery { mockSimpleHiitPreferences.getNumberOfWorkPeriods() } returns testValue
        //
        val actual = simpleHiitRepository.getNumberOfWorkPeriods()
        //
        coVerify (exactly = 1){ mockSimpleHiitPreferences.getNumberOfWorkPeriods() }
        assertEquals(testValue, actual)
    }

///////////////////

    @ParameterizedTest(name = "{index} -> when called with {0}, should call hiitPreferences with {0}")
    @ValueSource(booleans = [true, false])
    fun `setBeepSound calls hiitPreferences setBeepSound with correct value `(
        testValue: Boolean
    ) = runTest {
        coEvery { mockSimpleHiitPreferences.setBeepSound(any()) } just Runs
        //
        simpleHiitRepository.setBeepSound(testValue)
        //
        coVerify (exactly = 1){ mockSimpleHiitPreferences.setBeepSound(testValue) }
    }

    @ParameterizedTest(name = "{index} -> when hiitPreferences return {0}, should return {0}")
    @ValueSource(booleans = [true, false])
    fun `getBeepSound calls hiitPreferences getBeepSound and return correct value `(
        testValue: Boolean
    ) = runTest {
        coEvery { mockSimpleHiitPreferences.getBeepSoundActive() } returns testValue
        //
        val actual = simpleHiitRepository.getBeepSound()
        //
        coVerify (exactly = 1){ mockSimpleHiitPreferences.getBeepSoundActive() }
        assertEquals(testValue, actual)
    }

///////////////////

    @Test
    fun `setSessionStartCountdown calls hiitPreferences setSessionStartCountdown with correct value `() = runTest {
        val testValue = 123
        coEvery { mockSimpleHiitPreferences.setSessionStartCountdown(any()) } just Runs
        //
        simpleHiitRepository.setSessionStartCountdown(testValue)
        //
        coVerify (exactly = 1){ mockSimpleHiitPreferences.setSessionStartCountdown(testValue) }
    }

    @Test
    fun `getSessionStartCountdown calls hiitPreferences getSessionStartCountdown and return correct value `() = runTest {
        val testValue = 123
        coEvery { mockSimpleHiitPreferences.getSessionStartCountdown() } returns testValue
        //
        val actual = simpleHiitRepository.getSessionStartCountdown()
        //
        coVerify (exactly = 1){ mockSimpleHiitPreferences.getSessionStartCountdown() }
        assertEquals(testValue, actual)
    }

///////////////////

    @Test
    fun `setPeriodStartCountdown calls hiitPreferences setPeriodStartCountdown with correct value `() = runTest {
        val testValue = 123
        coEvery { mockSimpleHiitPreferences.setPeriodStartCountdown(any()) } just Runs
        //
        simpleHiitRepository.setPeriodStartCountdown(testValue)
        //
        coVerify (exactly = 1){ mockSimpleHiitPreferences.setPeriodStartCountdown(testValue) }
    }

    @Test
    fun `getPeriodStartCountdown calls hiitPreferences getPeriodStartCountdown and return correct value `() = runTest {
        val testValue = 123
        coEvery { mockSimpleHiitPreferences.getPeriodStartCountdown() } returns testValue
        //
        val actual = simpleHiitRepository.getPeriodStartCountdown()
        //
        coVerify (exactly = 1){ mockSimpleHiitPreferences.getPeriodStartCountdown() }
        assertEquals(testValue, actual)
    }

///////////////////

    @Test
    fun `setNumberOfCumulatedCycles calls hiitPreferences setNumberOfCumulatedCycles with correct value `() = runTest {
        val testValue = 123
        coEvery { mockSimpleHiitPreferences.setNumberOfCumulatedCycles(any()) } just Runs
        //
        simpleHiitRepository.setNumberOfCumulatedCycles(testValue)
        //
        coVerify (exactly = 1){ mockSimpleHiitPreferences.setNumberOfCumulatedCycles(testValue) }
    }

    @Test
    fun `getNumberOfCumulatedCycles calls hiitPreferences getNumberOfCumulatedCycles and return correct value `() = runTest {
        val testValue = 123
        coEvery { mockSimpleHiitPreferences.getNumberOfCumulatedCycles() } returns testValue
        //
        val actual = simpleHiitRepository.getNumberOfCumulatedCycles()
        //
        coVerify (exactly = 1){ mockSimpleHiitPreferences.getNumberOfCumulatedCycles() }
        assertEquals(testValue, actual)
    }

///////////////////

    @Test
    fun `setExercisesTypesSelected calls hiitPreferences setExercisesTypesSelected with correct value `() = runTest {
        val testValue = listOf(CAT, CRAB, LUNGE, LYING, PLANK, SITTING, SQUAT, STANDING)
        coEvery { mockSimpleHiitPreferences.setExercisesTypesSelected(any()) } just Runs
        //
        simpleHiitRepository.setExercisesTypesSelected(testValue)
        //
        coVerify (exactly = 1){ mockSimpleHiitPreferences.setExercisesTypesSelected(testValue) }
    }

    @Test
    fun `getExercisesTypesSelected calls hiitPreferences getExercisesTypesSelected and return correct value `() = runTest {
        val testValue = listOf(CAT, CRAB, LUNGE, LYING, PLANK, SITTING, SQUAT, STANDING)
        coEvery { mockSimpleHiitPreferences.getExercisesTypesSelected() } returns testValue
        //
        val actual = simpleHiitRepository.getExercisesTypesSelected()
        //
        coVerify (exactly = 1){ mockSimpleHiitPreferences.getExercisesTypesSelected() }
        assertEquals(testValue, actual)
    }
}