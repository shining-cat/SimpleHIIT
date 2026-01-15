/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.common.models

data class StepTimerState(
    val milliSecondsRemaining: Long = -1,
    val totalMilliSeconds: Long = -1,
) {
    /**
     * Calculates the remaining percentage safely, ensuring the result is always in 0.0-1.0 range.
     * Returns 0.0 for invalid states (negative values, zero total, etc.)
     */
    val remainingPercentage: Float
        get() {
            if (totalMilliSeconds <= 0 || milliSecondsRemaining < 0) return 0f
            val percentage = milliSecondsRemaining / totalMilliSeconds.toFloat()
            return percentage.coerceIn(0f, 1f)
        }
}
