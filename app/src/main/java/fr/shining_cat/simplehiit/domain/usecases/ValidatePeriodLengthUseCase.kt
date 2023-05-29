package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.ui.settings.SettingsViewState
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class ValidatePeriodLengthUseCase @Inject constructor(
    private val hiitLogger: HiitLogger
) {

    fun execute(input: String, periodCountDownLengthSeconds: Long): Constants.InputError {
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