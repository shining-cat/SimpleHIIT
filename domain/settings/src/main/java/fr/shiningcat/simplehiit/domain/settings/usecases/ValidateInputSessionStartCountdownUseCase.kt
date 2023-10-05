package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Constants
import javax.inject.Inject

class ValidateInputSessionStartCountdownUseCase @Inject constructor(
    private val hiitLogger: HiitLogger
) {

    fun execute(input: String): Constants.InputError {
        return if ((input.toLongOrNull() is Long).not()) {
            Constants.InputError.WRONG_FORMAT
        } else {
            Constants.InputError.NONE
        }
    }
}
