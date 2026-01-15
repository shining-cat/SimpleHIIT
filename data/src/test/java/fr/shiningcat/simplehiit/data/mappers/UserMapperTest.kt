/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.data.mappers

import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import org.junit.jupiter.api.Assertions.assertEquals
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
        expectedOutput: UserEntity,
    ) {
        val actual = userMapper.convert(input)
        assertEquals(expectedOutput, actual)
    }

    @ParameterizedTest(name = "{index} -> given {0} should return {1}")
    @MethodSource("userEntityMapperArguments")
    fun `converting from entity to model returns expected object`(
        input: UserEntity,
        expectedOutput: User,
    ) {
        val actual = userMapper.convert(input)
        assertEquals(expectedOutput, actual)
    }

    private companion object {
        @JvmStatic
        fun userModelMapperArguments(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    User(123L, "tralala", false, null),
                    UserEntity(123L, "tralala", false, null),
                ),
                Arguments.of(
                    User(345L, "trululu", true, null),
                    UserEntity(345L, "trululu", true, null),
                ),
                Arguments.of(
                    User(456L, "user with timestamp", true, 1234567890L),
                    UserEntity(456L, "user with timestamp", true, 1234567890L),
                ),
            )

        @JvmStatic
        fun userEntityMapperArguments() =
            Stream.of(
                Arguments.of(
                    UserEntity(123L, "tralala", false, null),
                    User(123L, "tralala", false, null),
                ),
                Arguments.of(
                    UserEntity(345L, "trululu", true, null),
                    User(345L, "trululu", true, null),
                ),
                Arguments.of(
                    UserEntity(456L, "user with timestamp", true, 1234567890L),
                    User(456L, "user with timestamp", true, 1234567890L),
                ),
            )
    }
}
