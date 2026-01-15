/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected

class ToggleExerciseTypeInListUseCase(
    private val logger: HiitLogger,
) {
    fun execute(
        currentList: List<ExerciseTypeSelected>,
        exerciseTypeToToggle: ExerciseTypeSelected,
    ): List<ExerciseTypeSelected> =
        currentList.map {
            if (it.type == exerciseTypeToToggle.type) {
                it.copy(selected = !it.selected)
            } else {
                it
            }
        }
}
