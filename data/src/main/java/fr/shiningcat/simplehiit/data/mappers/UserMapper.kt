package fr.shiningcat.simplehiit.data.mappers

import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity
import fr.shiningcat.simplehiit.domain.common.models.User

/**
 * For Koin: called directly as UserMapper() in dataModule
 */
class UserMapper {
    fun convert(userEntity: UserEntity): User =
        User(
            id = userEntity.userId,
            name = userEntity.name,
            selected = userEntity.selected,
            lastSessionTimestamp = userEntity.lastSessionTimestamp,
        )

    fun convert(userModel: User): UserEntity =
        UserEntity(
            userId = userModel.id,
            name = userModel.name,
            selected = userModel.selected,
            lastSessionTimestamp = userModel.lastSessionTimestamp,
        )
}
