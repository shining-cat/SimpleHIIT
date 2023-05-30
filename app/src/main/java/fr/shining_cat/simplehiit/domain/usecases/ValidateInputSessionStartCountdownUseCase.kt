package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class ValidateInputSessionStartCountdownUseCase @Inject constructor(
    private val hiitLogger: HiitLogger
) {

    fun execute(input: String): Constants.InputError {
        return if ((input.toLongOrNull() is Long).not()) {
            Constants.InputError.WRONG_FORMAT
        } else Constants.InputError.NONE
    }

}