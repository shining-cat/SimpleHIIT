/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.InputError

class ValidateInputSessionStartCountdownUseCase(
    private val logger: HiitLogger,
) {
    fun execute(input: String): InputError? =
        if ((input.toLongOrNull() is Long).not()) {
            InputError.WRONG_FORMAT
        } else {
            null
        }
}
