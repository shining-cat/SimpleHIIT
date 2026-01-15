/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.data.repositories

import fr.shiningcat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager
import fr.shiningcat.simplehiit.domain.common.SimpleHiitPreferencesFactory
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.SimpleHiitPreferences
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
internal class SettingsRepositoryImplTest : AbstractMockkTest() {
    private val mockSimpleHiitDataStoreManager = mockk<SimpleHiitDataStoreManager>()

    @Test
    fun `setWorkPeriodLength calls HiitDataStoreManager setWorkPeriodLength with correct value `() =
        runTest {
            val settingsRepository =
                SettingsRepositoryImpl(
                    simpleHiitDataStoreManager = mockSimpleHiitDataStoreManager,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            val testValue = 123L
            coEvery { mockSimpleHiitDataStoreManager.setWorkPeriodLength(any()) } just Runs
            //
            settingsRepository.setWorkPeriodLength(testValue)
            //
            coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.setWorkPeriodLength(testValue) }
        }

    @Test
    fun `setRestPeriodLength calls HiitDataStoreManager setRestPeriodLength with correct value `() =
        runTest {
            val settingsRepository =
                SettingsRepositoryImpl(
                    simpleHiitDataStoreManager = mockSimpleHiitDataStoreManager,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            val testValue = 123L
            coEvery { mockSimpleHiitDataStoreManager.setRestPeriodLength(any()) } just Runs
            //
            settingsRepository.setRestPeriodLength(testValue)
            //
            coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.setRestPeriodLength(testValue) }
        }

    @Test
    fun `setNumberOfWorkPeriods calls HiitDataStoreManager setNumberOfWorkPeriods with correct value `() =
        runTest {
            val settingsRepository =
                SettingsRepositoryImpl(
                    simpleHiitDataStoreManager = mockSimpleHiitDataStoreManager,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            val testValue = 123
            coEvery { mockSimpleHiitDataStoreManager.setNumberOfWorkPeriods(any()) } just Runs
            //
            settingsRepository.setNumberOfWorkPeriods(testValue)
            //
            coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.setNumberOfWorkPeriods(testValue) }
        }

    @ParameterizedTest(name = "{index} -> when called with {0}, should call HiitDataStoreManager with {0}")
    @ValueSource(booleans = [true, false])
    fun `setBeepSound calls HiitDataStoreManager setBeepSound with correct value `(testValue: Boolean) =
        runTest {
            val settingsRepository =
                SettingsRepositoryImpl(
                    simpleHiitDataStoreManager = mockSimpleHiitDataStoreManager,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            coEvery { mockSimpleHiitDataStoreManager.setBeepSound(any()) } just Runs
            //
            settingsRepository.setBeepSound(testValue)
            //
            coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.setBeepSound(testValue) }
        }

    @Test
    fun `setSessionStartCountdown calls HiitDataStoreManager setSessionStartCountdown with correct value `() =
        runTest {
            val settingsRepository =
                SettingsRepositoryImpl(
                    simpleHiitDataStoreManager = mockSimpleHiitDataStoreManager,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            val testValue = 123L
            coEvery { mockSimpleHiitDataStoreManager.setSessionStartCountdown(any()) } just Runs
            //
            settingsRepository.setSessionStartCountdown(testValue)
            //
            coVerify(exactly = 1) {
                mockSimpleHiitDataStoreManager.setSessionStartCountdown(
                    testValue,
                )
            }
        }

    @Test
    fun `setPeriodStartCountdown calls HiitDataStoreManager setPeriodStartCountdown with correct value `() =
        runTest {
            val settingsRepository =
                SettingsRepositoryImpl(
                    simpleHiitDataStoreManager = mockSimpleHiitDataStoreManager,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            val testValue = 123L
            coEvery { mockSimpleHiitDataStoreManager.setPeriodStartCountdown(any()) } just Runs
            //
            settingsRepository.setPeriodStartCountdown(testValue)
            //
            coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.setPeriodStartCountdown(testValue) }
        }

    @Test
    fun `setNumberOfCumulatedCycles calls HiitDataStoreManager setNumberOfCumulatedCycles with correct value `() =
        runTest {
            val settingsRepository =
                SettingsRepositoryImpl(
                    simpleHiitDataStoreManager = mockSimpleHiitDataStoreManager,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            val testValue = 123
            coEvery { mockSimpleHiitDataStoreManager.setNumberOfCumulatedCycles(any()) } just Runs
            //
            settingsRepository.setTotalRepetitionsNumber(testValue)
            //
            coVerify(exactly = 1) {
                mockSimpleHiitDataStoreManager.setNumberOfCumulatedCycles(
                    testValue,
                )
            }
        }

    @Test
    fun `setExercisesTypesSelected calls HiitDataStoreManager setExercisesTypesSelected with correct value `() =
        runTest {
            val settingsRepository =
                SettingsRepositoryImpl(
                    simpleHiitDataStoreManager = mockSimpleHiitDataStoreManager,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            val testValue =
                listOf(
                    ExerciseType.CAT,
                    ExerciseType.CRAB,
                    ExerciseType.LUNGE,
                    ExerciseType.LYING,
                    ExerciseType.PLANK,
                    ExerciseType.SITTING,
                    ExerciseType.SQUAT,
                    ExerciseType.STANDING,
                )
            coEvery { mockSimpleHiitDataStoreManager.setExercisesTypesSelected(any()) } just Runs
            //
            settingsRepository.setExercisesTypesSelected(testValue)
            //
            coVerify(exactly = 1) {
                mockSimpleHiitDataStoreManager.setExercisesTypesSelected(
                    testValue,
                )
            }
        }

    @Test
    fun `setAppTheme calls HiitDataStoreManager setAppTheme with correct value `() =
        runTest {
            val settingsRepository =
                SettingsRepositoryImpl(
                    simpleHiitDataStoreManager = mockSimpleHiitDataStoreManager,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            val testValue = AppTheme.DARK
            coEvery { mockSimpleHiitDataStoreManager.setAppTheme(any()) } just Runs
            //
            settingsRepository.setAppTheme(testValue)
            //
            coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.setAppTheme(testValue) }
        }

    @Test
    fun `getGeneralSettings calls HiitDataStoreManager getGeneralSettings, return correct value and updates returned flow content`() =
        runTest {
            val settingsRepository =
                SettingsRepositoryImpl(
                    simpleHiitDataStoreManager = mockSimpleHiitDataStoreManager,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            val datastoreOutput1 =
                SimpleHiitPreferencesFactory.createDefault() // holds default values, should always be the fallback
            val datastoreOutput2 =
                SimpleHiitPreferences(
                    workPeriodLengthMs = 123,
                    restPeriodLengthMs = 234,
                    numberOfWorkPeriods = 345,
                    beepSoundActive = true,
                    sessionCountDownLengthMs = 456,
                    PeriodCountDownLengthMs = 567,
                    selectedExercisesTypes = randomListOfExerciseTypesSelected(),
                    numberCumulatedCycles = 3,
                    appTheme = AppTheme.FOLLOW_SYSTEM,
                )
            val datastoreOutput3 =
                SimpleHiitPreferences(
                    workPeriodLengthMs = 987,
                    restPeriodLengthMs = 876,
                    numberOfWorkPeriods = 765,
                    beepSoundActive = false,
                    sessionCountDownLengthMs = 654,
                    PeriodCountDownLengthMs = 543,
                    selectedExercisesTypes = randomListOfExerciseTypesSelected(),
                    numberCumulatedCycles = 5,
                    appTheme = AppTheme.FOLLOW_SYSTEM,
                )
            val settingsFlow = MutableSharedFlow<SimpleHiitPreferences>()
            coEvery { mockSimpleHiitDataStoreManager.getPreferences() } answers { settingsFlow }
            //
            val settingsFlowAsList = mutableListOf<SimpleHiitPreferences>()
            val collectJob =
                launch(UnconfinedTestDispatcher()) {
                    settingsRepository.getPreferences().toList(settingsFlowAsList)
                }
            coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.getPreferences() }
            // first emission
            settingsFlow.emit(datastoreOutput1)
            assertEquals(1, settingsFlowAsList.size)
            val settings1 = settingsFlowAsList[0]
            assertEquals(datastoreOutput1, settings1)
            // second emission
            settingsFlow.emit(datastoreOutput2)
            assertEquals(2, settingsFlowAsList.size)
            val settings2 = settingsFlowAsList[1]
            assertEquals(datastoreOutput2, settings2)
            // third emission
            settingsFlow.emit(datastoreOutput3)
            assertEquals(3, settingsFlowAsList.size)
            val settings3 = settingsFlowAsList[2]
            assertEquals(datastoreOutput3, settings3)
            //
            collectJob.cancel()
        }

    private fun randomListOfExerciseTypesSelected() =
        ExerciseType.entries.map {
            ExerciseTypeSelected(
                type = it,
                selected = Random.nextBoolean(),
            )
        }

    @Test
    fun `getGeneralSettings swallows datastore exception and returns default value`() =
        runTest {
            val settingsRepository =
                SettingsRepositoryImpl(
                    simpleHiitDataStoreManager = mockSimpleHiitDataStoreManager,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            val defaultSettings =
                SimpleHiitPreferencesFactory.createDefault() // holds default values, should always be the fallback
            val thrownException = Exception("this is a test exception")
            coEvery { mockSimpleHiitDataStoreManager.getPreferences() } throws thrownException
            //
            val settingsFlowAsList = mutableListOf<SimpleHiitPreferences>()
            val collectJob =
                launch(UnconfinedTestDispatcher()) {
                    settingsRepository.getPreferences().toList(settingsFlowAsList)
                }
            coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.getPreferences() }
            coVerify(exactly = 1) {
                mockHiitLogger.e(
                    any(),
                    "failed getting general settings - returning default settings",
                    thrownException,
                )
            }
            assertEquals(1, settingsFlowAsList.size)
            val settingsOutput = settingsFlowAsList[0]
            assertEquals(defaultSettings, settingsOutput)
            //
            collectJob.cancel()
        }

// ////////////////

    @Test
    fun `resetAllSettings calls HiitDataStoreManager clearAll`() =
        runTest {
            val settingsRepository =
                SettingsRepositoryImpl(
                    simpleHiitDataStoreManager = mockSimpleHiitDataStoreManager,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            coEvery { mockSimpleHiitDataStoreManager.clearAll() } just Runs
            //
            settingsRepository.resetAllSettings()
            //
            coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.clearAll() }
        }
}
