package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class ValidateInputNumberCyclesUseCase @Inject constructor(
    private val hiitLogger: HiitLogger
) {

    fun execute(input: String): Constants.InputError {
        return if (input.length < 3 && input.toIntOrNull() is Int) Constants.InputError.NONE else Constants.InputError.WRONG_FORMAT
    }

}