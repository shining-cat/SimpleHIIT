/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.statistics

import fr.shiningcat.simplehiit.commonutils.NonEmptyList
import fr.shiningcat.simplehiit.commonutils.TimeProvider
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.DisplayedStatistic
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.domain.common.models.UserStatistics
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.jupiter.api.BeforeEach

/**
 * Base class for StatisticsPresenter tests.
 * Provides common setup, mocks, and helper methods.
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class StatisticsPresenterTestBase : AbstractMockkTest() {
    protected val mockStatisticsInteractor = mockk<StatisticsInteractor>()
    protected val mockMapper = mockk<StatisticsViewStateMapper>()
    protected val mockTimeProvider = mockk<TimeProvider>()

    protected val usersFlow =
        MutableStateFlow<Output<NonEmptyList<User>>>(
            Output.Success(testUsers()),
        )

    protected val testUser1 = User(id = 1L, name = "User One", selected = true)
    protected val testUser2 = User(id = 2L, name = "User Two", selected = false)

    protected lateinit var testedPresenter: StatisticsPresenter

    protected fun testUsers() = NonEmptyList(testUser1, listOf(testUser2))

    protected fun testUserStatistics() =
        UserStatistics(
            user = testUser1,
            totalNumberOfSessions = 10,
            cumulatedTimeOfExerciseMs = 9000000L,
            averageSessionLengthMs = 900000L,
            longestStreakDays = 7,
            currentStreakDays = 3,
            averageNumberOfSessionsPerWeek = "2.5",
        )

    protected fun testNominalViewState() =
        StatisticsViewState.Nominal(
            user = testUser1,
            statistics = emptyList<DisplayedStatistic>(),
            showUsersSwitch = true,
        )

    @BeforeEach
    fun setUp() {
        every { mockStatisticsInteractor.getAllUsers() } returns usersFlow
        every { mockTimeProvider.getCurrentTimeMillis() } returns 123456789L

        testedPresenter =
            StatisticsPresenter(
                statisticsInteractor = mockStatisticsInteractor,
                mapper = mockMapper,
                timeProvider = mockTimeProvider,
                logger = mockHiitLogger,
            )
    }
}
