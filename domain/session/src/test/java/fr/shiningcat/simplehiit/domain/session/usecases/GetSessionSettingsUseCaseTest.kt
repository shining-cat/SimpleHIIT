/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.session.usecases

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.SimpleHiitPreferencesFactory
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import fr.shiningcat.simplehiit.domain.common.datainterfaces.UsersRepository
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.SessionSettings
import fr.shiningcat.simplehiit.domain.common.models.SimpleHiitPreferences
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetSessionSettingsUseCaseTest : AbstractMockkTest() {
    private val mockUsersRepository = mockk<UsersRepository>()
    private val mockSettingsRepository = mockk<SettingsRepository>()
    private val testedUseCase =
        GetSessionSettingsUseCase(
            mockUsersRepository,
            mockSettingsRepository,
            mockHiitLogger,
        )

    @Test
    fun `calls repo and return success with correct order of values`() =
        runTest(UnconfinedTestDispatcher()) {
            val settingsValue1 = SimpleHiitPreferencesFactory.createDefault()
            val settingsValue2 =
                SimpleHiitPreferences(
                    workPeriodLengthMs = 123,
                    restPeriodLengthMs = 234,
                    numberOfWorkPeriods = 345,
                    beepSoundActive = true,
                    sessionCountDownLengthMs = 456,
                    PeriodCountDownLengthMs = 567,
                    selectedExercisesTypes = randomListOfExerciseTypesSelected(),
                    numberCumulatedCycles = 5,
                    appTheme = AppTheme.FOLLOW_SYSTEM,
                )
            val settingsValue3 =
                SimpleHiitPreferences(
                    workPeriodLengthMs = 321,
                    restPeriodLengthMs = 432,
                    numberOfWorkPeriods = 543,
                    beepSoundActive = false,
                    sessionCountDownLengthMs = 654,
                    PeriodCountDownLengthMs = 765,
                    selectedExercisesTypes = randomListOfExerciseTypesSelected(),
                    numberCumulatedCycles = 3,
                    appTheme = AppTheme.FOLLOW_SYSTEM,
                )
            val settingsFlow = MutableSharedFlow<SimpleHiitPreferences>()
            coEvery { mockSettingsRepository.getPreferences() } answers { settingsFlow }
            //
            val user1 = User(id = 123L, name = "user 1 name", selected = true)
            val user2 = User(id = 234L, name = "user 2 name", selected = true)
            val user3 = User(id = 345L, name = "user 3 name", selected = true)
            val user4 = User(id = 456L, name = "user 4 name", selected = true)
            val usersList1 = Output.Success(listOf(user1, user3))
            val usersList2 = Output.Success(listOf(user1, user2, user4))
            val usersFlow = MutableSharedFlow<Output<List<User>>>()
            coEvery { mockUsersRepository.getSelectedUsers() } answers { usersFlow }
            //
            val generalSettingsFlowAsList = mutableListOf<Output<SessionSettings>>()
            val collectJob =
                launch {
                    testedUseCase.execute().toList(generalSettingsFlowAsList)
                }
            //
            settingsFlow.emit(settingsValue1)
            usersFlow.emit(usersList1)
            // on the first emission, combine will wait to have both before emitting the result
            assertEquals(1, generalSettingsFlowAsList.size)
            val homeSettingsResult1 = generalSettingsFlowAsList.last()
            val expectedResult1 =
                Output.Success(
                    SessionSettings(
                        numberCumulatedCycles = settingsValue1.numberCumulatedCycles,
                        workPeriodLengthMs = settingsValue1.workPeriodLengthMs,
                        restPeriodLengthMs = settingsValue1.restPeriodLengthMs,
                        numberOfWorkPeriods = settingsValue1.numberOfWorkPeriods,
                        cycleLengthMs = 240000L,
                        beepSoundCountDownActive = settingsValue1.beepSoundActive,
                        sessionStartCountDownLengthMs = settingsValue1.sessionCountDownLengthMs,
                        periodsStartCountDownLengthMs = settingsValue1.PeriodCountDownLengthMs,
                        users = usersList1.result,
                        exerciseTypes = settingsValue1.selectedExercisesTypes,
                    ),
                )
            assertEquals(expectedResult1, homeSettingsResult1)
            //
            settingsFlow.emit(settingsValue2)
            assertEquals(2, generalSettingsFlowAsList.size)
            // on subsequent emissions, combine will immediately emit the result for every input flow emission. Here one more is expected
            val homeSettingsResult2 = generalSettingsFlowAsList.last()
            val expectedResult2 =
                Output.Success(
                    SessionSettings(
                        numberCumulatedCycles = settingsValue2.numberCumulatedCycles,
                        workPeriodLengthMs = settingsValue2.workPeriodLengthMs,
                        restPeriodLengthMs = settingsValue2.restPeriodLengthMs,
                        numberOfWorkPeriods = settingsValue2.numberOfWorkPeriods,
                        cycleLengthMs = 123165L,
                        beepSoundCountDownActive = settingsValue2.beepSoundActive,
                        sessionStartCountDownLengthMs = settingsValue2.sessionCountDownLengthMs,
                        periodsStartCountDownLengthMs = settingsValue2.PeriodCountDownLengthMs,
                        users = usersList1.result,
                        exerciseTypes = settingsValue2.selectedExercisesTypes,
                    ),
                )
            assertEquals(expectedResult2, homeSettingsResult2)
            //
            settingsFlow.emit(settingsValue3)
            usersFlow.emit(usersList2)
            assertEquals(4, generalSettingsFlowAsList.size)
            // on subsequent emissions, combine will immediately emit the result for every input flow emission. Here TWO more are expected
            val homeSettingsResult3 = generalSettingsFlowAsList.last()
            val expectedResult3 =
                Output.Success(
                    SessionSettings(
                        numberCumulatedCycles = settingsValue3.numberCumulatedCycles,
                        workPeriodLengthMs = settingsValue3.workPeriodLengthMs,
                        restPeriodLengthMs = settingsValue3.restPeriodLengthMs,
                        numberOfWorkPeriods = settingsValue3.numberOfWorkPeriods,
                        cycleLengthMs = 408879L,
                        beepSoundCountDownActive = settingsValue3.beepSoundActive,
                        sessionStartCountDownLengthMs = settingsValue3.sessionCountDownLengthMs,
                        periodsStartCountDownLengthMs = settingsValue3.PeriodCountDownLengthMs,
                        users = usersList2.result,
                        exerciseTypes = settingsValue3.selectedExercisesTypes,
                    ),
                )
            assertEquals(expectedResult3, homeSettingsResult3)
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
    fun `calls repo and return error if repo returns error for Users`() =
        runTest(UnconfinedTestDispatcher()) {
            val settingsValue =
                SimpleHiitPreferences(
                    workPeriodLengthMs = 123,
                    restPeriodLengthMs = 234,
                    numberOfWorkPeriods = 345,
                    beepSoundActive = true,
                    sessionCountDownLengthMs = 456,
                    PeriodCountDownLengthMs = 567,
                    selectedExercisesTypes = randomListOfExerciseTypesSelected(),
                    numberCumulatedCycles = 5,
                    appTheme = AppTheme.FOLLOW_SYSTEM,
                )
            val settingsFlow = MutableSharedFlow<SimpleHiitPreferences>()
            coEvery { mockSettingsRepository.getPreferences() } answers { settingsFlow }
            //
            val testException = Exception("this is a test exception")
            val usersError = Output.Error(DomainError.DATABASE_FETCH_FAILED, testException)
            val usersFlow = MutableSharedFlow<Output<List<User>>>()
            coEvery { mockUsersRepository.getSelectedUsers() } answers { usersFlow }
            //
            val generalSettingsFlowAsList = mutableListOf<Output<SessionSettings>>()
            val collectJob =
                launch {
                    testedUseCase.execute().toList(generalSettingsFlowAsList)
                }
            //
            settingsFlow.emit(settingsValue)
            usersFlow.emit(usersError)
            assertEquals(1, generalSettingsFlowAsList.size)
            val homeSettingsResult = generalSettingsFlowAsList[0]
            val expectedError = Output.Error(DomainError.NO_USERS_FOUND, testException)
            assertEquals(expectedError, homeSettingsResult)
            //
            collectJob.cancel()
        }
}
