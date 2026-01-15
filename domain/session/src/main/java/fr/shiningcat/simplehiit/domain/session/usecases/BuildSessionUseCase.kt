/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.session.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.AsymmetricalExerciseSideOrder
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide
import fr.shiningcat.simplehiit.domain.common.models.Session
import fr.shiningcat.simplehiit.domain.common.models.SessionSettings
import fr.shiningcat.simplehiit.domain.common.models.SessionStep
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class BuildSessionUseCase(
    private val composeExercisesListForSessionUseCase: ComposeExercisesListForSessionUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    private val logger: HiitLogger,
) {
    suspend fun execute(sessionSettings: SessionSettings): Session =
        withContext(defaultDispatcher) {
            val totalSessionLengthMs =
                sessionSettings.cycleLengthMs
                    .times(sessionSettings.numberCumulatedCycles)
                    .plus(sessionSettings.sessionStartCountDownLengthMs)
            val exercisesList = getSelectedExercisesTypesList(sessionSettings)
            val steps =
                buildStepsList(
                    exercisesList = exercisesList,
                    restPeriodLengthMs = sessionSettings.restPeriodLengthMs,
                    workPeriodLengthMs = sessionSettings.workPeriodLengthMs,
                    periodsStartCountDownLengthMs = sessionSettings.periodsStartCountDownLengthMs,
                    sessionStartCountDownLengthMs = sessionSettings.sessionStartCountDownLengthMs,
                )
            Session(
                steps = steps,
                durationMs = totalSessionLengthMs,
                beepSoundCountDownActive = sessionSettings.beepSoundCountDownActive,
                users = sessionSettings.users,
            )
        }

    private suspend fun getSelectedExercisesTypesList(sessionSettings: SessionSettings): List<Exercise> =
        withContext(defaultDispatcher) {
            val selectedExerciseTypes =
                sessionSettings.exerciseTypes.filter { it.selected }.map { it.type }
            composeExercisesListForSessionUseCase.execute(
                numberOfWorkPeriodsPerCycle = sessionSettings.numberOfWorkPeriods,
                numberOfCycles = sessionSettings.numberCumulatedCycles,
                selectedExerciseTypes = selectedExerciseTypes,
            )
        }

    private suspend fun buildStepsList(
        exercisesList: List<Exercise>,
        restPeriodLengthMs: Long,
        workPeriodLengthMs: Long,
        periodsStartCountDownLengthMs: Long,
        sessionStartCountDownLengthMs: Long,
    ): List<SessionStep> =
        withContext(defaultDispatcher) {
            val allSteps = mutableListOf<SessionStep>()
            val totalSteps = exercisesList.size
            if (sessionStartCountDownLengthMs > 0L) {
                val totalSessionDurationMs =
                    totalSteps.times(workPeriodLengthMs.plus(restPeriodLengthMs))
                val prepareStep =
                    SessionStep.PrepareStep(
                        durationMs = sessionStartCountDownLengthMs,
                        remainingSessionDurationMsAfterMe = totalSessionDurationMs,
                        countDownLengthMs = sessionStartCountDownLengthMs,
                    )
                allSteps.add(prepareStep)
            }
            for ((index, exercise) in exercisesList.withIndex()) {
                // we're not checking the asymmetrical attribute here as it has already been taking into consideration while building this exercisesList by adding asymmetrical exercises twice.
                // we have to check the asymmetrical attribute first in case a non-asymmetrical exercise has been added twice in a row by picking into the same list several times (when we need more exercises than the selected ones contain)
                val stepExerciseSide =
                    if (exercise.asymmetrical) {
                        when (exercise) {
                            exercisesList.getOrNull(index - 1) -> {
                                // if previous exercise was the same, then we are handling an asymmetrical for the second side
                                AsymmetricalExerciseSideOrder.SECOND.side
                            }
                            exercisesList.getOrNull(index + 1) -> {
                                // if next exercise will be the same, then we are handling an asymmetrical for the first side
                                AsymmetricalExerciseSideOrder.FIRST.side
                            }
                            else -> { // this should not happen
                                logger.e(
                                    "BuildSessionStepsList",
                                    "Error while identifying asymmetrical exercises",
                                )
                                ExerciseSide.NONE
                            }
                        }
                    } else { // symmetrical exercise
                        ExerciseSide.NONE
                    }

                //
                val remainingExercisesAfterStep = totalSteps.minus(index).minus(1)
                val remainingSessionDurationMsAfterRest =
                    (remainingExercisesAfterStep.plus(1)).times(workPeriodLengthMs) +
                        (remainingExercisesAfterStep).times(
                            restPeriodLengthMs,
                        )
//                hiitLogger.d("BuildSessionUseCase","buildStepsList::remainingSessionDurationMsAfterRest = $remainingSessionDurationMsAfterRest")
                val remainingSessionDurationMsAfterWork =
                    remainingExercisesAfterStep.times(workPeriodLengthMs.plus(restPeriodLengthMs))
//                hiitLogger.d("BuildSessionUseCase","buildStepsList::remainingSessionDurationMsAfterWork = $remainingSessionDurationMsAfterWork")
                //
                val restStep =
                    SessionStep.RestStep(
                        exercise = exercise,
                        side = stepExerciseSide,
                        durationMs = restPeriodLengthMs,
                        remainingSessionDurationMsAfterMe = remainingSessionDurationMsAfterRest,
                        countDownLengthMs = periodsStartCountDownLengthMs,
                    )
                allSteps.add(restStep)
                //
                val workStep =
                    SessionStep.WorkStep(
                        exercise = exercise,
                        side = stepExerciseSide,
                        durationMs = workPeriodLengthMs,
                        remainingSessionDurationMsAfterMe = remainingSessionDurationMsAfterWork,
                        countDownLengthMs = periodsStartCountDownLengthMs,
                    )
                allSteps.add(workStep)
            }
            logger.d("BuildSessionStepsList", "execute::input exercisesList : $exercisesList")
            logger.d("BuildSessionStepsList", "execute::output stepsList : $allSteps")
            allSteps.toList()
        }
}
