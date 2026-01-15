/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.common.models

data class HomeSettings(
    val numberCumulatedCycles: Int,
    val cycleLengthMs: Long,
    val users: List<User>,
    val warning: LaunchSessionWarning? = null,
)
