package fr.shiningcat.simplehiit.data.mappers

import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity
import fr.shiningcat.simplehiit.domain.common.models.User
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun convert(userEntity: UserEntity): User {
        return User(id = userEntity.userId, name = userEntity.name, selected = userEntity.selected)
    }

    fun convert(userModel: User): UserEntity {
        return UserEntity(userId = userModel.id, name = userModel.name, selected = userModel.selected)
    }
}
