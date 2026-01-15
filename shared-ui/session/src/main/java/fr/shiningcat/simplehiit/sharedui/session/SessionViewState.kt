/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.session

import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide
import fr.shiningcat.simplehiit.domain.common.models.SessionStepDisplay

sealed interface SessionViewState {
    object Loading : SessionViewState

    data class InitialCountDownSession(
        val countDown: CountDown,
    ) : SessionViewState

    data class RunningNominal(
        val periodType: RunningSessionStepType,
        val displayedExercise: Exercise,
        val side: ExerciseSide,
        val stepRemainingTime: String,
        val stepRemainingPercentage: Float,
        val sessionRemainingTime: String,
        val sessionRemainingPercentage: Float,
        val countDown: CountDown? = null,
    ) : SessionViewState

    data class Finished(
        val sessionDurationFormatted: String,
        val workingStepsDone: List<SessionStepDisplay>,
    ) : SessionViewState

    data class Error(
        val errorCode: String,
    ) : SessionViewState
}

enum class RunningSessionStepType { REST, WORK }

data class CountDown(
    val secondsDisplay: String,
    val progress: Float,
    val playBeep: Boolean,
)

sealed interface SessionDialog {
    object None : SessionDialog

    object Pause : SessionDialog
}
