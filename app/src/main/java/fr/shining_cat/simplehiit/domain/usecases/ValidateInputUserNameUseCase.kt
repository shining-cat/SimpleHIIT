package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class ValidateInputUserNameUseCase @Inject constructor(
    private val hiitLogger: HiitLogger
) {

    fun execute(user: User, existingUsers: List<User>): Constants.InputError {
        return if (existingUsers.find { it.name == user.name && it.id != user.id } != null) {
            Constants.InputError.VALUE_ALREADY_TAKEN
        } else if (user.name.length < 25) {
            Constants.InputError.NONE
        } else {
            Constants.InputError.TOO_LONG
        }
    }

}