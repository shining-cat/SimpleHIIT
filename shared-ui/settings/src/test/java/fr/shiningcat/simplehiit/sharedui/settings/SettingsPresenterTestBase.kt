/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.settings

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.GeneralSettings
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.jupiter.api.BeforeEach

/**
 * Base class for SettingsPresenter tests.
 * Provides shared setup, mocks, and helper methods.
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class SettingsPresenterTestBase : AbstractMockkTest() {
    protected val mockSettingsInteractor = mockk<SettingsInteractor>()
    protected val mockMapper = mockk<SettingsViewStateMapper>()
    protected val testDispatcher = StandardTestDispatcher()

    protected val generalSettingsFlow = MutableStateFlow<Output<GeneralSettings>>(Output.Success(testGeneralSettings()))

    protected val testUser1 = User(id = 1L, name = "User One", selected = true)
    protected val testUser2 = User(id = 2L, name = "User Two", selected = false)

    protected lateinit var testedPresenter: SettingsPresenter

    protected fun testGeneralSettings() =
        GeneralSettings(
            workPeriodLengthMs = 20000L,
            restPeriodLengthMs = 10000L,
            numberOfWorkPeriods = 8,
            cycleLengthMs = 30000L,
            beepSoundCountDownActive = true,
            sessionStartCountDownLengthMs = 10000L,
            periodsStartCountDownLengthMs = 5000L,
            users = listOf(testUser1, testUser2),
            exerciseTypes =
                listOf(
                    ExerciseTypeSelected(ExerciseType.LUNGE, true),
                    ExerciseTypeSelected(ExerciseType.PLANK, false),
                ),
            currentLanguage = AppLanguage.ENGLISH,
            currentTheme = AppTheme.FOLLOW_SYSTEM,
        )

    protected fun testNominalViewState() =
        SettingsViewState.Nominal(
            workPeriodLengthAsSeconds = "20",
            restPeriodLengthAsSeconds = "10",
            numberOfWorkPeriods = "8",
            totalCycleLength = "30",
            beepSoundCountDownActive = true,
            sessionStartCountDownLengthAsSeconds = "10",
            periodsStartCountDownLengthAsSeconds = "5",
            users = listOf(testUser1, testUser2),
            exerciseTypes =
                listOf(
                    ExerciseTypeSelected(ExerciseType.LUNGE, true),
                    ExerciseTypeSelected(ExerciseType.PLANK, false),
                ),
            currentLanguage = AppLanguage.ENGLISH,
            currentTheme = AppTheme.FOLLOW_SYSTEM,
        )

    @BeforeEach
    fun setUp() {
        every { mockSettingsInteractor.getGeneralSettings() } returns generalSettingsFlow
        every { mockMapper.map(any()) } returns SettingsViewState.Loading

        testedPresenter =
            SettingsPresenter(
                settingsInteractor = mockSettingsInteractor,
                mapper = mockMapper,
                dispatcher = testDispatcher,
                logger = mockHiitLogger,
            )
    }
}
