package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.models.User

class ValidateInputUserNameUseCase(
    private val logger: HiitLogger,
) {
    fun execute(
        user: User,
        existingUsers: List<User>,
    ): Constants.InputError =
        when {
            user.name.isBlank() -> {
                Constants.InputError.VALUE_EMPTY
            }
            existingUsers.find { it.name == user.name && it.id != user.id } != null -> {
                Constants.InputError.VALUE_ALREADY_TAKEN
            }
            user.name.length < 25 -> {
                Constants.InputError.NONE
            }
            else -> {
                Constants.InputError.TOO_LONG
            }
        }
}
