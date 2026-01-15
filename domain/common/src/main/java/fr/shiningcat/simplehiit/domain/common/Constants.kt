/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.common

import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected

object Constants {
    object SettingsDefaultValues {
        const val WORK_PERIOD_LENGTH_MILLISECONDS_DEFAULT = 20000L
        const val REST_PERIOD_LENGTH_MILLISECONDS_DEFAULT = 10000L
        const val NUMBER_WORK_PERIODS_DEFAULT = 8
        const val BEEP_SOUND_ACTIVE_DEFAULT = true
        const val SESSION_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT = 15000L
        const val PERIOD_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT = 5000L
        const val NUMBER_CUMULATED_CYCLES_DEFAULT = 1
        val DEFAULT_SELECTED_EXERCISES_TYPES =
            ExerciseType.entries.map { ExerciseTypeSelected(it, true) }
        val DEFAULT_APP_THEME = AppTheme.FOLLOW_SYSTEM
    }

    const val NO_RESULTS_FOUND = "no results found"
}
