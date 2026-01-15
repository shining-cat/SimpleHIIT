/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.common.models

/**
 * Application preferences/settings.
 * Pure data model without default values.
 * Use SimpleHiitPreferencesFactory in domain:common to create instances with defaults.
 */
data class SimpleHiitPreferences(
    val workPeriodLengthMs: Long,
    val restPeriodLengthMs: Long,
    val numberOfWorkPeriods: Int,
    val beepSoundActive: Boolean,
    val sessionCountDownLengthMs: Long,
    val PeriodCountDownLengthMs: Long,
    val selectedExercisesTypes: List<ExerciseTypeSelected>,
    val numberCumulatedCycles: Int,
    val appTheme: AppTheme,
)
