/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.InputError

class ValidatePeriodLengthUseCase(
    private val logger: HiitLogger,
) {
    fun execute(
        input: String,
        periodCountDownLengthSeconds: Long,
    ): InputError? {
        if ((input.toLongOrNull() is Long).not()) {
            return InputError.WRONG_FORMAT
        }
        val periodLengthSeconds = input.toLong()
        if (periodLengthSeconds < periodCountDownLengthSeconds) {
            return InputError.VALUE_TOO_SMALL
        }
        return null
    }
}
