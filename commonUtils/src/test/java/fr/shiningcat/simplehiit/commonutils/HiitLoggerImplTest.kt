package fr.shiningcat.simplehiit.commonutils

import android.util.Log
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class HiitLoggerImplTest {
    private val testTag = "This is a test TAG"
    private val testMessage = "This is a test MESSAGE"
    private val testThrowable = Exception("This is a test Throwable")

    @BeforeEach
    fun setupBeforeEach() {
        mockkStatic(Log::class)
        val tagSlot = slot<String>()
        val messageSlot = slot<String>()
        val throwableSlot = slot<Throwable>()
        coEvery { Log.d(capture(tagSlot), capture(messageSlot)) } answers {
            println("Log.d :: " + tagSlot.captured + " | " + messageSlot.captured)
            1
        }
        coEvery { Log.e(capture(tagSlot), capture(messageSlot)) } answers {
            println("Log.e :: " + tagSlot.captured + " | " + messageSlot.captured)
            1
        }
        coEvery { Log.e(capture(tagSlot), capture(messageSlot), capture(throwableSlot)) } answers {
            println("Log.e :: " + tagSlot.captured + " | " + messageSlot.captured + " | " + throwableSlot.captured)
            1
        }
    }

    @AfterEach
    fun cleanUp() {
        clearAllMocks()
    }

    @After
    fun wipeOut() {
        unmockkAll()
    }

    @Test
    fun `HiitLogger d calls Log d if debug is true with correct tag and message`() =
        runTest {
            val hiitLogger = HiitLoggerImpl(true)
            //
            hiitLogger.d(testTag, testMessage)
            //
            verify(exactly = 1) { Log.d("SIMPLEHIIT", "$testTag::$testMessage") }
        }

    @Test
    fun `HiitLogger d does NOT calls Log d if debug is false`() =
        runTest {
            val hiitLogger = HiitLoggerImpl(false)
            //
            hiitLogger.d(testTag, testMessage)
            //
            verify(exactly = 0) { Log.d(any(), any()) }
        }

    @Test
    fun `HiitLogger e calls Log e if debug is true with correct tag and message`() =
        runTest {
            val hiitLogger = HiitLoggerImpl(true)
            //
            hiitLogger.e(testTag, testMessage)
            //
            verify(exactly = 1) { Log.e("SIMPLEHIIT", "$testTag::$testMessage") }
        }

    @Test
    fun `HiitLogger e calls Log e if debug is true with correct tag, message, and throwable`() =
        runTest {
            val hiitLogger = HiitLoggerImpl(true)
            //
            hiitLogger.e(testTag, testMessage, testThrowable)
            //
            verify(exactly = 1) { Log.e("SIMPLEHIIT", "$testTag::$testMessage", testThrowable) }
        }

    @Test
    fun `HiitLogger e(tag, msg) does NOT call Log e if debug is false`() =
        runTest {
            val hiitLogger = HiitLoggerImpl(false)
            //
            hiitLogger.e(testTag, testMessage)
            //
            verify(exactly = 0) { Log.e(any(), any()) }
        }

    @Test
    fun `HiitLogger e(tag, msg, throwable) does NOT call Log e if debug is false`() =
        runTest {
            val hiitLogger = HiitLoggerImpl(false)
            //
            hiitLogger.e(testTag, testMessage, testThrowable)
            //
            verify(exactly = 0) { Log.e(any(), any(), any()) }
        }
}
