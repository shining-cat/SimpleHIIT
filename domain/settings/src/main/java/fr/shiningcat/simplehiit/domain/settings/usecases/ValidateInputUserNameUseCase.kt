package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.InputError
import fr.shiningcat.simplehiit.domain.common.models.User

class ValidateInputUserNameUseCase(
    private val logger: HiitLogger,
) {
    fun execute(
        user: User,
        existingUsers: List<User>,
    ): InputError? =
        when {
            user.name.isBlank() -> {
                InputError.VALUE_EMPTY
            }
            existingUsers.find { it.name == user.name && it.id != user.id } != null -> {
                InputError.VALUE_ALREADY_TAKEN
            }
            user.name.length < 25 -> {
                null
            }
            else -> {
                InputError.TOO_LONG
            }
        }
}
