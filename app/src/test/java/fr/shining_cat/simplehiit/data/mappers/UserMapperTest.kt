package fr.shining_cat.simplehiit.data.mappers

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity
import fr.shining_cat.simplehiit.domain.models.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class UserMapperTest : AbstractMockkTest() {

    private val userMapper = UserMapper()


    @Test
    fun `converting from model to entity returns expected object`() {
        val expectedOutput = UserEntity(
            userId = 123L,
            name = "tralala"
        )
        val model = User(
            id = 123L,
            name = "tralala"
        )
        val actual = userMapper.convert(model)
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `converting from entity to model returns expected object`() {
        val entity = UserEntity(
            userId = 123L,
            name = "tralala"
        )
        val expectedOutput = User(
            id = 123L,
            name = "tralala"
        )
        val actual = userMapper.convert(entity)
        assertEquals(expectedOutput, actual)
    }

}