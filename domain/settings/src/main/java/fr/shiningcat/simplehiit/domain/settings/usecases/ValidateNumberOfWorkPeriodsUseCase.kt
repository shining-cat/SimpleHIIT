package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Constants
import javax.inject.Inject

class ValidateNumberOfWorkPeriodsUseCase
    @Inject
    constructor(
        private val hiitLogger: HiitLogger,
    ) {
        fun execute(input: String): Constants.InputError =
            if ((input.toIntOrNull() is Int).not()) {
                Constants.InputError.WRONG_FORMAT
            } else {
                Constants.InputError.NONE
            }
    }
