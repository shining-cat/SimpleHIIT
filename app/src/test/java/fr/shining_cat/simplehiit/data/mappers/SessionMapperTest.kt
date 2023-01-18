package fr.shining_cat.simplehiit.data.mappers

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity
import fr.shining_cat.simplehiit.domain.models.Session
import fr.shining_cat.simplehiit.domain.models.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class SessionMapperTest : AbstractMockkTest() {

    private val sessionMapper = SessionMapper()

    @ParameterizedTest(name = "{index} -> given {0} should return {1}")
    @MethodSource("sessionMapperArguments")
    fun`converting from model to entity returns expected object`(
        input: Session,
        expectedOutput:SessionEntity
    ){
        val actual = sessionMapper.convert(input)
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `converting from entity to model returns expected object`(){
        val entity = SessionEntity(
            sessionId = 123L,
            date = 78696L,
            durationMs = 345L
        )
        val expectedOutput = Session(
            id = 123L,
            date = 78696L,
            duration = 345L,
            users = emptyList()
        )
        val actual = sessionMapper.convert(entity)
        assertEquals(expectedOutput, actual)
    }

    private companion object {

        @JvmStatic
        fun sessionMapperArguments() =
            Stream.of(
                Arguments.of(
                    Session(
                        id = 123L,
                        date = 78696L,
                        duration = 345L,
                        users = emptyList()
                    ),
                    SessionEntity(
                        sessionId = 123L,
                        date = 78696L,
                        durationMs = 345L
                    )
                ),
                Arguments.of(
                    Session(
                        id = 123L,
                        date = 78696L,
                        duration = 345L,
                        users = listOf(
                            User( id = 123L, name = "tralala")
                        )
                    ),
                    SessionEntity(
                        sessionId = 123L,
                        date = 78696L,
                        durationMs = 345L
                    )
                ),
                Arguments.of(
                    Session(
                        id = 123L,
                        date = 78696L,
                        duration = 345L,
                        users = listOf(
                            User( id = 123L, name = "tralala"),
                            User( id = 234L, name = "trilili"),
                            User( id = 345L, name = "trololo"),
                            User( id = 456L, name = "trululu")
                        )
                    ),
                    SessionEntity(
                        sessionId = 123L,
                        date = 78696L,
                        durationMs = 345L
                    )
                ),
            )
    }
}