package fr.shining_cat.simplehiit

import fr.shining_cat.simplehiit.utils.HiitLogger
import io.mockk.*
import org.junit.After
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class AbstractMockkTest {

    protected val mockHiitLogger = mockk<HiitLogger>()

    @BeforeEach
    open fun setupBeforeEach() {
        val tagSlot = slot<String>()
        val messageSlot = slot<String>()
        val exceptionMessagingLogSlot = slot<Throwable>()

        coEvery {
            mockHiitLogger.d(
                tag = capture(tagSlot),
                msg = capture(messageSlot)
            )
        } answers { println("mockHiitLogger.d::tag:" + tagSlot.captured + " | message:" + messageSlot.captured) }
        //
        coEvery {
            mockHiitLogger.e(
                tag = capture(tagSlot),
                msg = capture(messageSlot),
                throwable = capture(exceptionMessagingLogSlot)
            )
        } answers { println("mockHiitLogger.e ::tag:" + tagSlot.captured + " | message:" + messageSlot.captured + " throwable: " + exceptionMessagingLogSlot.captured) }
        //
        coEvery {
            mockHiitLogger.e(
                tag = capture(tagSlot),
                msg = capture(messageSlot),
                throwable = null
            )
        } answers { println("mockHiitLogger.e ::tag:" + tagSlot.captured + " | message:" + messageSlot.captured) }
    }

    @AfterEach
    fun cleanUp() {
        clearAllMocks()
    }

    @After
    fun wipeOut() {
        unmockkAll()
    }
}