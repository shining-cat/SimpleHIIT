package fr.shiningcat.simplehiit.domain.home.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Constants

class ValidateInputNumberCyclesUseCase(
    private val logger: HiitLogger,
) {
    /**
     * Validates the input for number of cycles.
     * @param input The input string to validate
     * @return InputError.NONE if valid (1-2 digits, non-negative integer), otherwise InputError.WRONG_FORMAT
     */
    fun execute(input: String): Constants.InputError {
        val parsedValue = input.toIntOrNull()
        return if (input.length < 3 && parsedValue != null && parsedValue >= 0) {
            Constants.InputError.NONE
        } else {
            Constants.InputError.WRONG_FORMAT
        }
    }
}
