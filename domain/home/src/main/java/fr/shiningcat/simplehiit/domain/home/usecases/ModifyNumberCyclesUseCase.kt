package fr.shiningcat.simplehiit.domain.home.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.CyclesModification

/**
 * Validates and calculates the new number of cycles based on a modification request.
 * Ensures the resulting value is always positive.
 */
class ModifyNumberCyclesUseCase(
    private val logger: HiitLogger,
) {
    /**
     * Executes the modification logic.
     *
     * @param currentValue The current number of cycles
     * @param modification Whether to increase or decrease
     * @return Success with new value, or Error if modification would result in invalid value
     */
    fun execute(
        currentValue: Int,
        modification: CyclesModification,
    ): Result {
        val change =
            when (modification) {
                CyclesModification.INCREASE -> 1
                CyclesModification.DECREASE -> -1
            }

        val newValue = currentValue + change

        return if (newValue > 0) {
            logger.d(
                "ModifyNumberCyclesUseCase",
                "Modification ${modification.name} successful: $currentValue -> $newValue",
            )
            Result.Success(newValue)
        } else {
            logger.e(
                "ModifyNumberCyclesUseCase",
                "Modification ${modification.name} would result in non-positive value: $currentValue + $change = $newValue",
            )
            Result.Error.WouldBeNonPositive
        }
    }

    sealed class Result {
        data class Success(
            val newValue: Int,
        ) : Result()

        sealed class Error : Result() {
            object WouldBeNonPositive : Error()
        }
    }
}
