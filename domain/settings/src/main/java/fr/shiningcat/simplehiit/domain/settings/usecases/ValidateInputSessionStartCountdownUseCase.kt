package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Constants

class ValidateInputSessionStartCountdownUseCase(
    private val logger: HiitLogger,
) {
    fun execute(input: String): Constants.InputError =
        if ((input.toLongOrNull() is Long).not()) {
            Constants.InputError.WRONG_FORMAT
        } else {
            Constants.InputError.NONE
        }
}
