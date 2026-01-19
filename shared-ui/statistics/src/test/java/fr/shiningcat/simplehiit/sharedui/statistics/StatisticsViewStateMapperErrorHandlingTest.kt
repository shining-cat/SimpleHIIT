/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.statistics

import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Error handling tests for StatisticsViewStateMapper.
 * Focuses on error code mapping and edge cases.
 */
internal class StatisticsViewStateMapperErrorHandlingTest : AbstractMockkTest() {
    private val mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase =
        mockk<FormatLongDurationMsAsSmallestHhMmSsStringUseCase>()

    private val testedMapper =
        StatisticsViewStateMapper(
            formatLongDurationMsAsSmallestHhMmSsStringUseCase = mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase,
            logger = mockHiitLogger,
        )

    @Test
    fun `mapUsersError with NO_USERS_FOUND returns NoUsers state`() {
        val result = testedMapper.mapUsersError(DomainError.NO_USERS_FOUND)

        assertEquals(StatisticsViewState.NoUsers, result)
    }

    @Test
    fun `mapUsersError with DATABASE_FETCH_FAILED returns FatalError state`() {
        val result = testedMapper.mapUsersError(DomainError.DATABASE_FETCH_FAILED)

        assertTrue(result is StatisticsViewState.FatalError)
        assertEquals(DomainError.DATABASE_FETCH_FAILED.code, (result as StatisticsViewState.FatalError).errorCode)
    }

    @Test
    fun `mapUsersError with DATABASE_INSERT_FAILED returns FatalError state`() {
        val result = testedMapper.mapUsersError(DomainError.DATABASE_INSERT_FAILED)

        assertTrue(result is StatisticsViewState.FatalError)
        assertEquals(DomainError.DATABASE_INSERT_FAILED.code, (result as StatisticsViewState.FatalError).errorCode)
    }

    @Test
    fun `mapUsersError with EMPTY_RESULT returns FatalError state`() {
        val result = testedMapper.mapUsersError(DomainError.EMPTY_RESULT)

        assertTrue(result is StatisticsViewState.FatalError)
        assertEquals(DomainError.EMPTY_RESULT.code, (result as StatisticsViewState.FatalError).errorCode)
    }

    @Test
    fun `mapUsersError with LANGUAGE_SET_FAILED returns FatalError state`() {
        val result = testedMapper.mapUsersError(DomainError.LANGUAGE_SET_FAILED)

        assertTrue(result is StatisticsViewState.FatalError)
        assertEquals(DomainError.LANGUAGE_SET_FAILED.code, (result as StatisticsViewState.FatalError).errorCode)
    }
}
