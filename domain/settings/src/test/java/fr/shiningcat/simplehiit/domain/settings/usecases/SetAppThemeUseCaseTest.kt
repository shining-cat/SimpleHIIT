/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@OptIn(ExperimentalCoroutinesApi::class)
internal class SetAppThemeUseCaseTest : AbstractMockkTest() {
    private val mockSettingsRepository = mockk<SettingsRepository>()

    @ParameterizedTest(name = "{index} -> when called with {0}, should call SettingsRepository with {0}")
    @EnumSource(AppTheme::class)
    fun `calls repo with corresponding value`(testTheme: AppTheme) =
        runTest {
            val testedUseCase =
                SetAppThemeUseCase(
                    settingsRepository = mockSettingsRepository,
                )
            coEvery { mockSettingsRepository.setAppTheme(any()) } returns Unit
            //
            testedUseCase.execute(testTheme)
            //
            coVerify(exactly = 1) { mockSettingsRepository.setAppTheme(testTheme) }
        }
}
