/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.shared.core

import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class NavigationViewModelTest : AbstractMockkTest() {
    private fun buildViewModel() = NavigationViewModel(hiitLogger = mockHiitLogger)

    @Test
    fun `initial back stack contains only Home`() {
        val viewModel = buildViewModel()
        assertEquals(listOf(Screen.Home), viewModel.backStack.toList())
    }

    @Test
    fun `navigateTo pushes destination onto the back stack`() {
        val viewModel = buildViewModel()

        viewModel.navigateTo(Screen.Session)

        assertEquals(listOf(Screen.Home, Screen.Session), viewModel.backStack.toList())
    }

    @Test
    fun `clearAndNavigateTo resets the back stack to the single destination`() {
        val viewModel = buildViewModel()
        viewModel.navigateTo(Screen.Session)

        viewModel.clearAndNavigateTo(Screen.Settings)

        assertEquals(listOf(Screen.Settings), viewModel.backStack.toList())
    }

    @Test
    fun `goBack removes the last entry and returns true when not at root`() {
        val viewModel = buildViewModel()
        viewModel.navigateTo(Screen.Session)

        val result = viewModel.goBack()

        assertTrue(result)
        assertEquals(listOf(Screen.Home), viewModel.backStack.toList())
    }

    @Test
    fun `goBack returns false and keeps root when already at root`() {
        val viewModel = buildViewModel()

        val result = viewModel.goBack()

        assertFalse(result)
        assertEquals(listOf(Screen.Home), viewModel.backStack.toList())
    }
}
