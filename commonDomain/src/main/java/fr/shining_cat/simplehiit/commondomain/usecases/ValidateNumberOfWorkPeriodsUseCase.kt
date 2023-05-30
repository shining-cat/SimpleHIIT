package fr.shining_cat.simplehiit.commondomain.usecases

import fr.shining_cat.simplehiit.commondomain.Constants
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import javax.inject.Inject

class ValidateNumberOfWorkPeriodsUseCase @Inject constructor(
    private val hiitLogger: HiitLogger
) {

    fun execute(input: String): Constants.InputError {
        return if ((input.toIntOrNull() is Int).not()) {
            Constants.InputError.WRONG_FORMAT
        } else Constants.InputError.NONE
    }

}