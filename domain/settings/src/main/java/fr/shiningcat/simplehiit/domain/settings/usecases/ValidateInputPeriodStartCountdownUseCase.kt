package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Constants
import javax.inject.Inject

class ValidateInputPeriodStartCountdownUseCase
    @Inject
    constructor(
        private val hiitLogger: HiitLogger,
    ) {
        fun execute(
            input: String,
            workPeriodLengthSeconds: Long,
            restPeriodLengthSeconds: Long,
        ): Constants.InputError {
            if ((input.toLongOrNull() is Long).not()) {
                return Constants.InputError.WRONG_FORMAT
            }
            val periodCountDownLengthSeconds = input.toLong()
            if (workPeriodLengthSeconds < periodCountDownLengthSeconds || restPeriodLengthSeconds < periodCountDownLengthSeconds) {
                return Constants.InputError.VALUE_TOO_BIG
            }
            return Constants.InputError.NONE
        }
    }
