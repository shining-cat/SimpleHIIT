package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.models.StepTimerState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class StepTimerUseCaseTest : AbstractMockkTest() {

    @Test
    fun `timer runs for expected duration and emits expected states in order`() = runTest {
        //TODO: test is broken since usecase changed. As usecase is still not reliable, waiting for independant study case to be done to fix first usecase then this test
        /*val testDispatcher = StandardTestDispatcher(testScheduler)
        val testedUseCase = StepTimerUseCase(testDispatcher, mockHiitLogger)
        val stepTimerStatesAsList = mutableListOf<StepTimerState>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            testedUseCase.timerStateFlow.toList(stepTimerStatesAsList)
        }
        //value in seconds for the whole simulated StepTimer run
        val totalSeconds = 1000
        //this is the ticking value used in the StepTimer: 1s
        val stepTimer = 1000L
        //this is the delay we wait to check the state emitted by the StepTimer's "onStart", it needs to be taken into account in the following steps check too
        val firstCheckDelay = 10L
        //launching:
        launch {
            testedUseCase.start(totalSeconds)
        }
        //check first initial emitted state:
        assertEquals(1, stepTimerStatesAsList.size)
        println("test initial state = ${stepTimerStatesAsList.last()} | number of values = ${stepTimerStatesAsList.size}")
        // time has not moved yet, so the state will in effect be the default value StepTimerState()
        // This doesn't affect real life use, as time wouldn't be suspended while calling StepTimerUseCase.start()
        assertEquals(StepTimerState(), stepTimerStatesAsList.last())
        // advance virtual time by a tiny bit to check the first state emitted when timer starts
        testDispatcher.scheduler.advanceTimeBy(firstCheckDelay)
        //check state just after calling start and moving time less than a timerStep: firstCheckDelay
        assertEquals(1, stepTimerStatesAsList.size)
        //assertEquals(StepTimerState(totalSeconds, totalSeconds), stepTimerStatesAsList.last())
        println("test state immediately after start call = ${stepTimerStatesAsList.last()} | number of values = ${stepTimerStatesAsList.size}")
        //check all steps:
        for (tick in 1..totalSeconds) {
            // check step initial conditions:
            val actualEmittedStatesCountBeforeAdvance = stepTimerStatesAsList.size
            val expectedEmittedStatesCountBeforeAdvance = tick.plus(1) //this will start the whole loop with 2: the default initial and the start one
            assertEquals(
                expectedEmittedStatesCountBeforeAdvance,
                actualEmittedStatesCountBeforeAdvance
            )
            // advance virtual time by stepTimer
            testDispatcher.scheduler.advanceTimeBy(stepTimer)
            // check resulting emissions
            val actualEmittedStatesCountAfterAdvance = stepTimerStatesAsList.size
            val expectedEmittedStatesCountAfterAdvance = tick.plus(2) //now we should have 1 more, the one emitted by advancing time by stepTimer
            println("test virtual time loop middle = $currentTime | lastState emitted = ${stepTimerStatesAsList.last()} | emittedStatesCountAfterAdvance = $actualEmittedStatesCountAfterAdvance")
            assertEquals(
                expectedEmittedStatesCountAfterAdvance,
                actualEmittedStatesCountAfterAdvance
            )
            val emittedState = stepTimerStatesAsList.last()
            val expectedRemainingTime = totalSeconds - tick
            val expectedTickState = StepTimerState(expectedRemainingTime, totalSeconds)
            assertEquals(expectedTickState, emittedState)
            //
            val expectedElapsedVirtualTime = tick.times(stepTimer).plus(firstCheckDelay)
            assertEquals(expectedElapsedVirtualTime, currentTime)
        }
        val expectedTotalVirtualTime = totalSeconds.times(stepTimer).plus(firstCheckDelay)
        assertEquals(expectedTotalVirtualTime, currentTime)
        //
        collectJob.cancel()*/
    }
}