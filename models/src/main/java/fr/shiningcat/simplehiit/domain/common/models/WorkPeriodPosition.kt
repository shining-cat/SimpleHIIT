/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.common.models

/**
 * Position of a work period inside the overall session, stamped at build time.
 * 1-based. For rest steps this describes the *upcoming* work period.
 */
data class WorkPeriodPosition(
    val workPeriodInCycle: Int,
    val totalWorkPeriodsInCycle: Int,
    val cycle: Int,
    val totalCycles: Int,
)
