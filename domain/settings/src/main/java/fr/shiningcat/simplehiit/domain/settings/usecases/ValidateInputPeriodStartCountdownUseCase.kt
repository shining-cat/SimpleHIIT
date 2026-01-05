package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.InputError

class ValidateInputPeriodStartCountdownUseCase(
    private val logger: HiitLogger,
) {
    fun execute(
        input: String,
        workPeriodLengthSeconds: Long,
        restPeriodLengthSeconds: Long,
    ): InputError {
        if ((input.toLongOrNull() is Long).not()) {
            return InputError.WRONG_FORMAT
        }
        val periodCountDownLengthSeconds = input.toLong()
        if (workPeriodLengthSeconds < periodCountDownLengthSeconds || restPeriodLengthSeconds < periodCountDownLengthSeconds) {
            return InputError.VALUE_TOO_BIG
        }
        return InputError.NONE
    }
}
