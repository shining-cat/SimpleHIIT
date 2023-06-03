package fr.shining_cat.simplehiit.commondomain.usecases

import fr.shining_cat.simplehiit.commondomain.Constants
import fr.shining_cat.simplehiit.commondomain.models.User
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import javax.inject.Inject

class ValidateInputUserNameUseCase @Inject constructor(
    private val hiitLogger: HiitLogger
) {

    fun execute(
        user: User,
        existingUsers: List<User>
    ): Constants.InputError {
        return if (existingUsers.find { it.name == user.name && it.id != user.id } != null) {
            Constants.InputError.VALUE_ALREADY_TAKEN
        } else if (user.name.length < 25) {
            Constants.InputError.NONE
        } else {
            Constants.InputError.TOO_LONG
        }
    }

}