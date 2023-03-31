package fr.shining_cat.simplehiit.data.mappers

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity
import fr.shining_cat.simplehiit.domain.models.User
import hilt_aggregated_deps._dagger_hilt_android_internal_lifecycle_DefaultViewModelFactories_FragmentEntryPoint
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class UserMapperTest : AbstractMockkTest() {

    private val userMapper = UserMapper()

    @ParameterizedTest(name = "{index} -> given {0} should return {1}")
    @MethodSource("userModelMapperArguments")
    fun `converting from model to entity returns expected object`(
        input: User,
        expectedOutput: UserEntity
    ) {
        val actual = userMapper.convert(input)
        assertEquals(expectedOutput, actual)
    }

    @ParameterizedTest(name = "{index} -> given {0} should return {1}")
    @MethodSource("userEntityMapperArguments")
    fun `converting from entity to model returns expected object`(
        input: UserEntity,
        expectedOutput: User
    ) {
        val actual = userMapper.convert(input)
        assertEquals(expectedOutput, actual)
    }

    private companion object {

        @JvmStatic
        fun userModelMapperArguments() =
            Stream.of(
                Arguments.of(
                    User(123L, "tralala", false),
                    UserEntity(123L, "tralala", false)
                ),
                Arguments.of(
                    User(345L, "trululu", true),
                    UserEntity(345L, "trululu", true)
                )
            )

        @JvmStatic
        fun userEntityMapperArguments() =
            Stream.of(
                Arguments.of(
                    UserEntity(123L, "tralala", false),
                    User(123L, "tralala", false)
                ),
                Arguments.of(
                    UserEntity(345L, "trululu", true),
                    User(345L, "trululu", true)
                )
            )
    }

}