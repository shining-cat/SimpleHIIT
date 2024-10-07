package fr.shiningcat.simplehiit.testutils

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import io.mockk.unmockkAll
import org.junit.After
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

/**
 * This whole module is here to be added as a testImplementation dependency only
 * there is no way as of now to inherit from classes defined in the test folder of a module from other modules,
 * so the only workaround is to define it in the main sourceset, adding the test dependencies needed for it as
 * "normal" dependencies (using implementation calls instead of testImplementations)
 */
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
                msg = capture(messageSlot),
            )
        } answers { println("mockHiitLogger.d::tag:" + tagSlot.captured + " | message:" + messageSlot.captured) }
        //
        coEvery {
            mockHiitLogger.e(
                tag = capture(tagSlot),
                msg = capture(messageSlot),
                throwable = capture(exceptionMessagingLogSlot),
            )
        } answers
            {
                println(
                    "mockHiitLogger.e ::tag:" + tagSlot.captured + " | message:" + messageSlot.captured + " throwable: " +
                        exceptionMessagingLogSlot.captured,
                )
            }
        //
        coEvery {
            mockHiitLogger.e(
                tag = capture(tagSlot),
                msg = capture(messageSlot),
                throwable = null,
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
