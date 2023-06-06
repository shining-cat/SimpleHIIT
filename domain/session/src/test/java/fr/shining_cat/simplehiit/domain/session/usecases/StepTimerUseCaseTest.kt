package fr.shining_cat.simplehiit.domain.session.usecases

import fr.shining_cat.simplehiit.domain.common.models.StepTimerState
import fr.shining_cat.simplehiit.testutils.AbstractMockkTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class StepTimerUseCaseTest : AbstractMockkTest() {

    @Test
    fun `timer runs for expected duration and emits expected states in order`() = runTest() {
        //Special TimeProvider that increases the returned time every time it is queried
        val testTimeProvider = TestTimeProvider()
        //we don't want to use the unconfined dispatcher here as we don't want it to skip all delays
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val testedUseCase = fr.shining_cat.simplehiit.domain.session.usecases.StepTimerUseCase(
            timerDispatcher = testDispatcher,
            timeProvider = testTimeProvider,
            hiitLogger = mockHiitLogger
        )
        //
        val stepTimerStatesAsList = mutableListOf<StepTimerState>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            testedUseCase.timerStateFlow.toList(stepTimerStatesAsList)
        }
        //check initial state:
        assertEquals(1, stepTimerStatesAsList.size)
        assertEquals(StepTimerState(), stepTimerStatesAsList.last())
        //value in seconds for the whole simulated StepTimer run
        val totalMilliSeconds = 100000L
        //this is the ticking value used in the StepTimer: 1s
        val tickLength = 1000L
        //launching:
        testedUseCase.start(totalMilliSeconds)
        val expectedTicksCount = (1 + totalMilliSeconds.div(tickLength) + 1).toInt() // first default state + one every [tickLength]ms for [totalMilliSeconds]ms + the last one when reaching 0
        assertEquals(expectedTicksCount, stepTimerStatesAsList.size)
        val expectedStatesEmitted = mutableListOf(StepTimerState())
        for(i in 0..expectedTicksCount-2){
            expectedStatesEmitted.add(StepTimerState(totalMilliSeconds - tickLength.times(i), totalMilliSeconds))
        }
        assertEquals(expectedStatesEmitted, stepTimerStatesAsList)
        //
        collectJob.cancel()
    }
}

/**
 * This implementation of the TimeProvider will increase the returned time every time it is queried.
 * This is to prevent our StepTimer to loop on its own when we want to test it, as I could find no way
 * to manipulate the provided clock at the same time I was running test code out of its scope
 */
internal class TestTimeProvider(private val startClock: Long = 0L):
    fr.shining_cat.simplehiit.commonutils.TimeProvider {

    private var testClock = startClock

    fun advanceTestClock(howMuchMillis: Long = 1000L){
        testClock += howMuchMillis
    }

    override fun getCurrentTimeMillis(): Long {
        advanceTestClock(10L)
        return testClock
    }

}