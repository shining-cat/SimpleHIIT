/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.home.usecases

import fr.shiningcat.simplehiit.domain.common.models.CyclesModification
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ModifyNumberCyclesUseCaseTest : AbstractMockkTest() {
    private val testedUseCase = ModifyNumberCyclesUseCase(mockHiitLogger)

    @Test
    fun `execute with INCREASE returns success with incremented value`() {
        val currentValue = 5
        val result =
            testedUseCase.execute(
                currentValue = currentValue,
                modification = CyclesModification.INCREASE,
            )

        assertTrue(result is ModifyNumberCyclesUseCase.Result.Success)
        assertEquals(6, (result as ModifyNumberCyclesUseCase.Result.Success).newValue)
    }

    @Test
    fun `execute with DECREASE returns success with decremented value`() {
        val currentValue = 5
        val result =
            testedUseCase.execute(
                currentValue = currentValue,
                modification = CyclesModification.DECREASE,
            )

        assertTrue(result is ModifyNumberCyclesUseCase.Result.Success)
        assertEquals(4, (result as ModifyNumberCyclesUseCase.Result.Success).newValue)
    }

    @Test
    fun `execute with DECREASE from 1 returns error`() {
        val currentValue = 1
        val result =
            testedUseCase.execute(
                currentValue = currentValue,
                modification = CyclesModification.DECREASE,
            )

        assertTrue(result is ModifyNumberCyclesUseCase.Result.Error.WouldBeNonPositive)
    }

    @Test
    fun `execute with DECREASE from 0 returns error`() {
        val currentValue = 0
        val result =
            testedUseCase.execute(
                currentValue = currentValue,
                modification = CyclesModification.DECREASE,
            )

        assertTrue(result is ModifyNumberCyclesUseCase.Result.Error.WouldBeNonPositive)
    }

    @Test
    fun `execute with INCREASE from 0 returns success with value 1`() {
        val currentValue = 0
        val result =
            testedUseCase.execute(
                currentValue = currentValue,
                modification = CyclesModification.INCREASE,
            )

        assertTrue(result is ModifyNumberCyclesUseCase.Result.Success)
        assertEquals(1, (result as ModifyNumberCyclesUseCase.Result.Success).newValue)
    }

    @Test
    fun `execute with INCREASE from large value returns success`() {
        val currentValue = 99
        val result =
            testedUseCase.execute(
                currentValue = currentValue,
                modification = CyclesModification.INCREASE,
            )

        assertTrue(result is ModifyNumberCyclesUseCase.Result.Success)
        assertEquals(100, (result as ModifyNumberCyclesUseCase.Result.Success).newValue)
    }

    @Test
    fun `execute with DECREASE to exactly 1 returns success`() {
        val currentValue = 2
        val result =
            testedUseCase.execute(
                currentValue = currentValue,
                modification = CyclesModification.DECREASE,
            )

        assertTrue(result is ModifyNumberCyclesUseCase.Result.Success)
        assertEquals(1, (result as ModifyNumberCyclesUseCase.Result.Success).newValue)
    }
}
