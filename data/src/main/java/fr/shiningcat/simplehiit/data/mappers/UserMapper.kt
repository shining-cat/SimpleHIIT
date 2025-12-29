package fr.shiningcat.simplehiit.data.mappers

import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity
import fr.shiningcat.simplehiit.domain.common.models.User

/**
 * NO @Inject - provided explicitly by DataMappersModuleHilt via @Provides
 * For Koin: called directly as UserMapper() in dataModule
 */
class UserMapper {
    fun convert(userEntity: UserEntity): User = User(id = userEntity.userId, name = userEntity.name, selected = userEntity.selected)

    fun convert(userModel: User): UserEntity =
        UserEntity(
            userId = userModel.id,
            name = userModel.name,
            selected = userModel.selected,
        )
}
