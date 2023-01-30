package fr.shining_cat.simplehiit.data.mappers

import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity
import fr.shining_cat.simplehiit.domain.models.User
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun convert(userEntity: UserEntity):User{
        return User(id = userEntity.userId, name = userEntity.name, selected = userEntity.selected)
    }

    fun convert(userModel: User):UserEntity{
        return UserEntity(userId = userModel.id, name = userModel.name, selected = userModel.selected)
    }
}