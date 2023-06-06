package fr.shining_cat.simplehiit.domain.settings

import fr.shining_cat.simplehiit.domain.common.Constants
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import javax.inject.Inject

class ValidateInputPeriodStartCountdownUseCase @Inject constructor(
    private val hiitLogger: HiitLogger
) {

    fun execute(
        input: String,
        workPeriodLengthSeconds: Long,
        restPeriodLengthSeconds: Long
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