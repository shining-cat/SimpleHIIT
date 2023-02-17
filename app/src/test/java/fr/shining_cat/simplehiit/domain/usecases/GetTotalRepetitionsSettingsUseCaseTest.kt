package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.domain.models.SimpleHiitSettings
import fr.shining_cat.simplehiit.domain.models.TotalRepetitionsSetting
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
internal class GetTotalRepetitionsSettingsUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val testedUseCase = GetTotalRepetitionsSettingsUseCase(mockSimpleHiitRepository, mockHiitLogger)

    @Test
    fun `calls repo and return corresponding values in order`() = runTest {
        val repetitionsSettingValue1 = TotalRepetitionsSetting(numberCumulatedCycles = 2)
        val repetitionsSettingValue2 = TotalRepetitionsSetting(numberCumulatedCycles = 5)
        val settingsFlow = MutableSharedFlow<TotalRepetitionsSetting>()
        coEvery { mockSimpleHiitRepository.getTotalRepetitionsSetting() } answers { settingsFlow }
        //
        val settingsFlowAsList = mutableListOf<TotalRepetitionsSetting>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            testedUseCase.execute().toList(settingsFlowAsList)
        }
        coVerify(exactly = 1) { mockSimpleHiitRepository.getTotalRepetitionsSetting() }
        //
        settingsFlow.emit(repetitionsSettingValue1)
        assertEquals(1, settingsFlowAsList.size)
        val settings1 = settingsFlowAsList[0]
        assertEquals(repetitionsSettingValue1, settings1)
        //second emission
        settingsFlow.emit(repetitionsSettingValue2)
        assertEquals(2, settingsFlowAsList.size)
        val settings2 = settingsFlowAsList[1]
        assertEquals(repetitionsSettingValue2, settings2)
        //
        collectJob.cancel()
    }

}