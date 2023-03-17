package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import org.junit.jupiter.api.Assertions.*

internal class StepTimerUseCaseTest: AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()

}