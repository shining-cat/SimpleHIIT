package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.InputError

class ValidateNumberOfWorkPeriodsUseCase(
    private val logger: HiitLogger,
) {
    fun execute(input: String): InputError? =
        if ((input.toIntOrNull() is Int).not()) {
            InputError.WRONG_FORMAT
        } else {
            null
        }
}
