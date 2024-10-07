package fr.shiningcat.simplehiit.domain.home.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Constants
import javax.inject.Inject

class ValidateInputNumberCyclesUseCase
    @Inject
    constructor(
        private val hiitLogger: HiitLogger,
    ) {
        fun execute(input: String): Constants.InputError =
            if (input.length < 3 && input.toIntOrNull() is Int) Constants.InputError.NONE else Constants.InputError.WRONG_FORMAT
    }
