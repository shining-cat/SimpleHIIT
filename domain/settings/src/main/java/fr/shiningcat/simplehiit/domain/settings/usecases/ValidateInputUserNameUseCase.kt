package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.models.User
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
