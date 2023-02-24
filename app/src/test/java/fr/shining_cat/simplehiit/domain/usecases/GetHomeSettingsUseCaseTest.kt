package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetHomeSettingsUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val testedUseCase = GetHomeSettingsUseCase(mockSimpleHiitRepository, mockHiitLogger)

    @Test
    fun `calls repo and return success with correct order of values`() = runTest(UnconfinedTestDispatcher()) {
        val settingsValue1 = SimpleHiitPreferences()
        val settingsValue2 = SimpleHiitPreferences(
            workPeriodLengthMs = 123L,
            restPeriodLengthMs = 234L,
            numberOfWorkPeriods = 345,
            beepSoundActive = true,
            sessionCountDownLengthMs = 456L,
            PeriodCountDownLengthMs = 567L,
            selectedExercisesTypes = randomListOfExerciseTypesSelected(),
            numberCumulatedCycles = 5
        )
        val settingsValue3 = SimpleHiitPreferences(
            workPeriodLengthMs = 321L,
            restPeriodLengthMs = 432L,
            numberOfWorkPeriods = 543,
            beepSoundActive = false,
            sessionCountDownLengthMs = 654L,
            PeriodCountDownLengthMs = 765L,
            selectedExercisesTypes = randomListOfExerciseTypesSelected(),
            numberCumulatedCycles = 3
        )
        val settingsFlow = MutableSharedFlow<SimpleHiitPreferences>()
        coEvery { mockSimpleHiitRepository.getPreferences() } answers { settingsFlow }
        //
        val user1 = User(id = 123L, name = "user 1 name", selected = true)
        val user2 = User(id = 234L, name = "user 2 name", selected = true)
        val user3 = User(id = 345L, name = "user 3 name", selected = true)
        val user4 = User(id = 456L, name = "user 4 name", selected = true)
        val usersList1 = Output.Success(listOf(user1, user3))
        val usersList2 = Output.Success(listOf(user1, user2, user4))
        val usersFlow = MutableSharedFlow<Output<List<User>>>()
        coEvery { mockSimpleHiitRepository.getUsers() } answers { usersFlow }
        //
        val homeSettingsFlowAsList = mutableListOf<Output<HomeSettings>>()
        val collectJob = launch {
            testedUseCase.execute().toList(homeSettingsFlowAsList)
        }
        //
        settingsFlow.emit(settingsValue1)
        usersFlow.emit(usersList1)
        //on the first emission, combine will wait to have both before emitting the result
        assertEquals(1, homeSettingsFlowAsList.size)
        val homeSettingsResult1 = homeSettingsFlowAsList.last()
        val expectedResult1 = Output.Success(
            HomeSettings(
                numberCumulatedCycles = settingsValue1.numberCumulatedCycles,
                cycleLengthMs = 230000L,
                users = usersList1.result
            )
        )
        assertEquals(expectedResult1, homeSettingsResult1)
        //
        settingsFlow.emit(settingsValue2)
        assertEquals(2, homeSettingsFlowAsList.size)
        //on subsequent emissions, combine will immediately emit the result for every input flow emission. Here one more is expected
        val homeSettingsResult2 = homeSettingsFlowAsList.last()
        val expectedResult2 = Output.Success(
            HomeSettings(
                numberCumulatedCycles = settingsValue2.numberCumulatedCycles,
                cycleLengthMs = 122931L,
                users = usersList1.result
            )
        )
        assertEquals(expectedResult2, homeSettingsResult2)
        //
        settingsFlow.emit(settingsValue3)
        usersFlow.emit(usersList2)
        assertEquals(4, homeSettingsFlowAsList.size)
        //on subsequent emissions, combine will immediately emit the result for every input flow emission. Here TWO more are expected
        val homeSettingsResult3 = homeSettingsFlowAsList.last()
        val expectedResult3 = Output.Success(
            HomeSettings(
                numberCumulatedCycles = settingsValue3.numberCumulatedCycles,
                cycleLengthMs = 408447L,
                users = usersList2.result
            )
        )
        assertEquals(expectedResult3, homeSettingsResult3)
        //
        collectJob.cancel()
    }

    private fun randomListOfExerciseTypesSelected() = ExerciseType.values().toList().map {
        ExerciseTypeSelected(
            type = it,
            selected = Random.nextBoolean()
        )
    }

    @Test
    fun `calls repo and return error if repo returns error for Users`() = runTest(UnconfinedTestDispatcher()) {
        val settingsValue = SimpleHiitPreferences(
            workPeriodLengthMs = 123,
            restPeriodLengthMs = 234,
            numberOfWorkPeriods = 345,
            beepSoundActive = true,
            sessionCountDownLengthMs = 456,
            PeriodCountDownLengthMs = 567,
            selectedExercisesTypes = randomListOfExerciseTypesSelected(),
            numberCumulatedCycles = 5
        )
        val settingsFlow = MutableSharedFlow<SimpleHiitPreferences>()
        coEvery { mockSimpleHiitRepository.getPreferences() } answers { settingsFlow }
        //
        val testException = Exception("this is a test exception")
        val usersError = Output.Error(Constants.Errors.DATABASE_FETCH_FAILED, testException)
        val usersFlow = MutableSharedFlow<Output<List<User>>>()
        coEvery { mockSimpleHiitRepository.getUsers() } answers { usersFlow }
        //
        val homeSettingsFlowAsList = mutableListOf<Output<HomeSettings>>()
        val collectJob = launch {
            testedUseCase.execute().toList(homeSettingsFlowAsList)
        }
        //
        settingsFlow.emit(settingsValue)
        usersFlow.emit(usersError)
        assertEquals(1, homeSettingsFlowAsList.size)
        val homeSettingsResult = homeSettingsFlowAsList[0]
        val expectedError = Output.Error(Constants.Errors.NO_USERS_FOUND, testException)
        assertEquals(expectedError, homeSettingsResult)
        //
        collectJob.cancel()
    }

}