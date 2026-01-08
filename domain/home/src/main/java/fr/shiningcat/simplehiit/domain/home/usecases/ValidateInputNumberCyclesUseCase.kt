package fr.shiningcat.simplehiit.domain.home.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.InputError

class ValidateInputNumberCyclesUseCase(
    private val logger: HiitLogger,
) {
    /**
     * Validates the input for number of cycles.
     * @param input The input string to validate
     * @return null if valid (1-2 digits, non-negative integer), otherwise InputError.WRONG_FORMAT
     */
    fun execute(input: String): InputError? {
        val parsedValue = input.toIntOrNull()
        return if (input.length < 3 && parsedValue != null && parsedValue >= 0) {
            null
        } else {
            InputError.WRONG_FORMAT
        }
    }
}
