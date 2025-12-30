package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Constants

class ValidatePeriodLengthUseCase(
    private val logger: HiitLogger,
) {
    fun execute(
        input: String,
        periodCountDownLengthSeconds: Long,
    ): Constants.InputError {
        if ((input.toLongOrNull() is Long).not()) {
            return Constants.InputError.WRONG_FORMAT
        }
        val periodLengthSeconds = input.toLong()
        if (periodLengthSeconds < periodCountDownLengthSeconds) {
            return Constants.InputError.VALUE_TOO_SMALL
        }
        return Constants.InputError.NONE
    }
}
