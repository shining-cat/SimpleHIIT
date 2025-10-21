package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.domain.common.datainterfaces.LanguageRepository
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetCurrentAppLanguageUseCaseTest : AbstractMockkTest() {
    private val mockLanguageRepository = mockk<LanguageRepository>()

    @ParameterizedTest(name = "{index} -> should return flow with {0} from repository")
    @EnumSource(AppLanguage::class)
    fun `returns language flow from repository`(testLanguage: AppLanguage) =
        runTest {
            val testedUseCase =
                GetCurrentAppLanguageUseCase(
                    languageRepository = mockLanguageRepository,
                )
            every { mockLanguageRepository.getCurrentLanguage() } returns flowOf(testLanguage)
            //
            val result = testedUseCase.execute()
            //
            verify(exactly = 1) { mockLanguageRepository.getCurrentLanguage() }
            // Verify the flow returns the expected language
            result.collect { language ->
                assertEquals(testLanguage, language)
            }
        }
}
