package fr.shiningcat.simplehiit.domain.home.usecases

import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.HomeSettings
import fr.shiningcat.simplehiit.domain.common.models.SimpleHiitPreferences
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetHomeSettingsUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val testedUseCase = GetHomeSettingsUseCase(
        mockSimpleHiitRepository,
        mockHiitLogger
    )

    private val testSettingsValue = SimpleHiitPreferences(
        workPeriodLengthMs = 123,
        restPeriodLengthMs = 234,
        numberOfWorkPeriods = 345,
        beepSoundActive = true,
        sessionCountDownLengthMs = 456,
        PeriodCountDownLengthMs = 567,
        selectedExercisesTypes = randomListOfExerciseTypesSelected(),
        numberCumulatedCycles = 5
    )

    @Test
    fun `calls repo and return error if repo returns error for Users`() = runTest(UnconfinedTestDispatcher()) {
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
        settingsFlow.emit(testSettingsValue)
        usersFlow.emit(usersError)
        assertEquals(1, homeSettingsFlowAsList.size)
        val homeSettingsResult = homeSettingsFlowAsList[0]
        val expectedError = Output.Error(Constants.Errors.NO_USERS_FOUND, testException)
        assertEquals(expectedError, homeSettingsResult)
        //
        collectJob.cancel()
    }

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
        // on the first emission, combine will wait to have both before emitting the result
        assertEquals(1, homeSettingsFlowAsList.size)
        val homeSettingsResult1 = homeSettingsFlowAsList.last()
        val expectedResult1 = Output.Success(
            HomeSettings(
                numberCumulatedCycles = settingsValue1.numberCumulatedCycles,
                cycleLengthMs = 240000L,
                users = usersList1.result
            )
        )
        assertEquals(expectedResult1, homeSettingsResult1)
        //
        settingsFlow.emit(settingsValue2)
        assertEquals(2, homeSettingsFlowAsList.size)
        // on subsequent emissions, combine will immediately emit the result for every input flow emission. Here one more is expected
        val homeSettingsResult2 = homeSettingsFlowAsList.last()
        val expectedResult2 = Output.Success(
            HomeSettings(
                numberCumulatedCycles = settingsValue2.numberCumulatedCycles,
                cycleLengthMs = 123165L,
                users = usersList1.result
            )
        )
        assertEquals(expectedResult2, homeSettingsResult2)
        //
        settingsFlow.emit(settingsValue3)
        usersFlow.emit(usersList2)
        assertEquals(4, homeSettingsFlowAsList.size)
        // on subsequent emissions, combine will immediately emit the result for every input flow emission. Here TWO more are expected
        val homeSettingsResult3 = homeSettingsFlowAsList.last()
        val expectedResult3 = Output.Success(
            HomeSettings(
                numberCumulatedCycles = settingsValue3.numberCumulatedCycles,
                cycleLengthMs = 408879L,
                users = usersList2.result
            )
        )
        assertEquals(expectedResult3, homeSettingsResult3)
        //
        collectJob.cancel()
    }

    @Test
    fun `calls repo get single unselected user tries to toggle it but updating fails then return error`() = runTest(UnconfinedTestDispatcher()) {
        val settingsFlow = MutableSharedFlow<SimpleHiitPreferences>()
        coEvery { mockSimpleHiitRepository.getPreferences() } answers { settingsFlow }
        val updateException = Exception("this is a test exception")
        val updateError = Output.Error(errorCode = Constants.Errors.DATABASE_UPDATE_FAILED, exception = updateException)
        coEvery { mockSimpleHiitRepository.updateUser(any()) } answers { updateError }
        //
        val user1 = User(id = 123L, name = "user 1 name", selected = false)
        val usersList1 = Output.Success(listOf(user1))
        val usersFlow = MutableSharedFlow<Output<List<User>>>()
        coEvery { mockSimpleHiitRepository.getUsers() } answers { usersFlow }
        //
        val homeSettingsFlowAsList = mutableListOf<Output<HomeSettings>>()
        val collectJob = launch {
            testedUseCase.execute().toList(homeSettingsFlowAsList)
        }
        //
        usersFlow.emit(usersList1)
        settingsFlow.emit(testSettingsValue)
        //
        val user1ToggledToSelected = user1.copy(selected = true)
        coVerify(exactly = 1) { mockSimpleHiitRepository.updateUser(user1ToggledToSelected) }
        //
        assertEquals(1, homeSettingsFlowAsList.size)
        val homeSettingsResult = homeSettingsFlowAsList[0]
        val expectedError = Output.Error(Constants.Errors.DATABASE_UPDATE_FAILED, updateException)
        assertEquals(expectedError, homeSettingsResult)
        //
        collectJob.cancel()
    }

    @Test
    fun `calls repo get single unselected user tries to toggle it but updating is inconsistent then return error`() = runTest(UnconfinedTestDispatcher()) {
        val settingsFlow = MutableSharedFlow<SimpleHiitPreferences>()
        coEvery { mockSimpleHiitRepository.getPreferences() } answers { settingsFlow }
        val updateInconsistentSuccess = Output.Success(3) // we should only ever receive 1 if the update succeeds
        coEvery { mockSimpleHiitRepository.updateUser(any()) } answers { updateInconsistentSuccess }
        //
        val user1 = User(id = 123L, name = "user 1 name", selected = false)
        val usersList1 = Output.Success(listOf(user1))
        val usersFlow = MutableSharedFlow<Output<List<User>>>()
        coEvery { mockSimpleHiitRepository.getUsers() } answers { usersFlow }
        //
        val homeSettingsFlowAsList = mutableListOf<Output<HomeSettings>>()
        val collectJob = launch {
            testedUseCase.execute().toList(homeSettingsFlowAsList)
        }
        //
        usersFlow.emit(usersList1)
        settingsFlow.emit(testSettingsValue)
        //
        val user1ToggledToSelected = user1.copy(selected = true)
        coVerify(exactly = 1) { mockSimpleHiitRepository.updateUser(user1ToggledToSelected) }
        //
        assertEquals(1, homeSettingsFlowAsList.size)
        val homeSettingsResult = homeSettingsFlowAsList[0]
        assertTrue(homeSettingsResult is Output.Error)
        homeSettingsResult as Output.Error
        assertEquals(Constants.Errors.DATABASE_UPDATE_FAILED, homeSettingsResult.errorCode)
        assertEquals(Constants.Errors.DATABASE_UPDATE_FAILED.code, homeSettingsResult.exception.message)
        //
        collectJob.cancel()
    }

    @Test
    fun `calls repo get single unselected user tries to toggle it but still not getting any selected user then return error`() = runTest(UnconfinedTestDispatcher()) {
        val settingsFlow = MutableSharedFlow<SimpleHiitPreferences>()
        coEvery { mockSimpleHiitRepository.getPreferences() } answers { settingsFlow }
        val updateSuccess = Output.Success(1)
        coEvery { mockSimpleHiitRepository.updateUser(any()) } answers { updateSuccess }
        //
        val user1 = User(id = 123L, name = "user 1 name", selected = false)
        val usersList1 = Output.Success(listOf(user1))
        val usersFlow = MutableSharedFlow<Output<List<User>>>()
        coEvery { mockSimpleHiitRepository.getUsers() } answers { usersFlow }
        //
        val homeSettingsFlowAsList = mutableListOf<Output<HomeSettings>>()
        val collectJob = launch {
            // simulating the second call to cover failure during second attempt (after expected toggling to selected of the user)
            testedUseCase.execute().toList(homeSettingsFlowAsList)
        }
        //
        usersFlow.emit(usersList1)
        settingsFlow.emit(testSettingsValue)
        usersFlow.emit(usersList1) // triggering the second emission from getUsers, but it still only contains one non-selected user
        // there should still have been only one attempt to toggle the user
        coVerify(exactly = 1) { mockSimpleHiitRepository.updateUser(any()) }
        //
        assertEquals(1, homeSettingsFlowAsList.size)
        val homeSettingsResult = homeSettingsFlowAsList[0]
        assertTrue(homeSettingsResult is Output.Error)
        homeSettingsResult as Output.Error
        assertEquals(Constants.Errors.NO_SELECTED_USERS_FOUND, homeSettingsResult.errorCode)
        assertEquals(Constants.Errors.NO_SELECTED_USERS_FOUND.code, homeSettingsResult.exception.message)
        //
        collectJob.cancel()
    }

    @Test
    fun `calls repo and toggle single unselected user to selected if it is not then return success`() = runTest(UnconfinedTestDispatcher()) {
        val settingsValue = SimpleHiitPreferences(
            workPeriodLengthMs = 123L,
            restPeriodLengthMs = 234L,
            numberOfWorkPeriods = 345,
            beepSoundActive = true,
            sessionCountDownLengthMs = 456L,
            PeriodCountDownLengthMs = 567L,
            selectedExercisesTypes = randomListOfExerciseTypesSelected(),
            numberCumulatedCycles = 5
        )
        val settingsFlow = MutableSharedFlow<SimpleHiitPreferences>()
        coEvery { mockSimpleHiitRepository.getPreferences() } answers { settingsFlow }
        coEvery { mockSimpleHiitRepository.updateUser(any()) } answers { Output.Success(1) }
        //
        val user1 = User(id = 123L, name = "user 1 name", selected = false)
        val usersList1 = Output.Success(listOf(user1))
        val usersFlow = MutableSharedFlow<Output<List<User>>>()
        coEvery { mockSimpleHiitRepository.getUsers() } answers { usersFlow }
        //
        val homeSettingsFlowAsList = mutableListOf<Output<HomeSettings>>()
        val collectJob = launch {
            testedUseCase.execute().toList(homeSettingsFlowAsList)
        }
        //
        settingsFlow.emit(settingsValue)
        usersFlow.emit(usersList1)
        //
        val user1ToggledToSelected = user1.copy(selected = true)
        coVerify(exactly = 1) { mockSimpleHiitRepository.updateUser(user1ToggledToSelected) }
        //
        val usersList2 = Output.Success(listOf(user1ToggledToSelected))
        usersFlow.emit(usersList2)
        //
        assertEquals(1, homeSettingsFlowAsList.size)
        val homeSettingsResult = homeSettingsFlowAsList.last()
        val expectedResult = Output.Success(
            HomeSettings(
                numberCumulatedCycles = settingsValue.numberCumulatedCycles,
                cycleLengthMs = 123165L,
                users = usersList2.result
            )
        )
        assertEquals(expectedResult, homeSettingsResult)
        //
        collectJob.cancel()
    }

    private fun randomListOfExerciseTypesSelected() = ExerciseType.values().toList().map {
        ExerciseTypeSelected(
            type = it,
            selected = Random.nextBoolean()
        )
    }
}
