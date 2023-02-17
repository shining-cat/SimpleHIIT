package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.domain.models.SimpleHiitSettings
import fr.shining_cat.simplehiit.utils.HiitLogger
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetGeneralSettingsUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val testedUseCase = GetGeneralSettingsUseCase(mockSimpleHiitRepository, mockHiitLogger)

    @Test
    fun `calls repo and return corresponding values in order`() = runTest {
        val settingsValue1 = SimpleHiitSettings(
            workPeriodLength = 123,
            restPeriodLength = 234,
            numberOfWorkPeriods = 345,
            beepSoundActive = true,
            sessionCountDownLengthSeconds = 456,
            PeriodCountDownLengthSeconds = 567,
            selectedExercisesTypes = listOf(
                ExerciseType.CRAB,
                ExerciseType.LUNGE,
                ExerciseType.PLANK,
                ExerciseType.SITTING,
                ExerciseType.STANDING
            )
        )
        val settingsValue2 = SimpleHiitSettings(
            workPeriodLength = 987,
            restPeriodLength = 876,
            numberOfWorkPeriods = 765,
            beepSoundActive = false,
            sessionCountDownLengthSeconds = 654,
            PeriodCountDownLengthSeconds = 543,
            selectedExercisesTypes = listOf(
                ExerciseType.CAT,
                ExerciseType.CRAB,
                ExerciseType.LYING,
                ExerciseType.PLANK,
                ExerciseType.SQUAT
            )
        )
        val settingsFlow = MutableSharedFlow<SimpleHiitSettings>()
        coEvery { mockSimpleHiitRepository.getGeneralSettings() } answers { settingsFlow }
        //
        val settingsFlowAsList = mutableListOf<SimpleHiitSettings>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            testedUseCase.execute().toList(settingsFlowAsList)
        }
        coVerify(exactly = 1) { mockSimpleHiitRepository.getGeneralSettings() }
        //
        settingsFlow.emit(settingsValue1)
        assertEquals(1, settingsFlowAsList.size)
        val settings1 = settingsFlowAsList[0]
        assertEquals(settingsValue1, settings1)
        //second emission
        settingsFlow.emit(settingsValue2)
        assertEquals(2, settingsFlowAsList.size)
        val settings2 = settingsFlowAsList[1]
        assertEquals(settingsValue2, settings2)
        //
        collectJob.cancel()
    }

}