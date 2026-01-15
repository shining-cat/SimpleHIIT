/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.LanguageRepository
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@OptIn(ExperimentalCoroutinesApi::class)
internal class SetAppLanguageUseCaseTest : AbstractMockkTest() {
    private val mockLanguageRepository = mockk<LanguageRepository>()

    @ParameterizedTest(name = "{index} -> when called with {0}, should call LanguageRepository with {0} and return Output.Success")
    @EnumSource(AppLanguage::class)
    fun `calls repo with corresponding value and returns Output`(testLanguage: AppLanguage) =
        runTest {
            val testedUseCase =
                SetAppLanguageUseCase(
                    languageRepository = mockLanguageRepository,
                    logger = mockHiitLogger,
                )
            coEvery { mockLanguageRepository.setAppLanguage(any()) } returns Output.Success(1)
            //
            val result = testedUseCase.execute(testLanguage)
            //
            coVerify(exactly = 1) { mockLanguageRepository.setAppLanguage(testLanguage) }
            assertEquals(Output.Success(1), result)
        }
}
