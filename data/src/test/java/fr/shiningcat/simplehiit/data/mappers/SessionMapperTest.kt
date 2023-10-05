package fr.shiningcat.simplehiit.data.mappers

import fr.shiningcat.simplehiit.data.local.database.entities.SessionEntity
import fr.shiningcat.simplehiit.domain.common.models.SessionRecord
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class SessionMapperTest : AbstractMockkTest() {

    private val sessionMapper = SessionMapper()

    @ParameterizedTest(name = "{index} -> given {0} should return {1}")
    @MethodSource("sessionMapperArguments")
    fun `converting from model to entity returns expected list of objects`(
        input: SessionRecord,
        expectedOutput: List<SessionEntity>
    ) {
        val actual = sessionMapper.convert(input)
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `converting from entity to model returns expected object`() {
        val entity = SessionEntity(
            sessionId = 123L,
            timeStamp = 78696L,
            durationMs = 345L,
            userId = 1234L
        )
        val expectedOutput = SessionRecord(
            id = 123L,
            timeStamp = 78696L,
            durationMs = 345L,
            usersIds = listOf(1234L)
        )
        val actual = sessionMapper.convert(entity)
        assertEquals(expectedOutput, actual)
    }

    private companion object {

        @JvmStatic
        fun sessionMapperArguments(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    SessionRecord(
                        id = 123L,
                        timeStamp = 78696L,
                        durationMs = 345L,
                        usersIds = listOf(123L)
                    ),
                    listOf(
                        SessionEntity(
                            sessionId = 123L,
                            timeStamp = 78696L,
                            durationMs = 345L,
                            userId = 123L
                        )
                    )
                ),
                Arguments.of(
                    SessionRecord(
                        id = 123L,
                        timeStamp = 78696L,
                        durationMs = 345L,
                        usersIds = listOf(123L, 234L, 345L, 456L)
                    ),
                    listOf(
                        SessionEntity(
                            sessionId = 123L,
                            timeStamp = 78696L,
                            durationMs = 345L,
                            userId = 123L
                        ),
                        SessionEntity(
                            sessionId = 123L,
                            timeStamp = 78696L,
                            durationMs = 345L,
                            userId = 234L
                        ),
                        SessionEntity(
                            sessionId = 123L,
                            timeStamp = 78696L,
                            durationMs = 345L,
                            userId = 345L
                        ),
                        SessionEntity(
                            sessionId = 123L,
                            timeStamp = 78696L,
                            durationMs = 345L,
                            userId = 456L
                        )
                    ),
                )
            )
    }
}