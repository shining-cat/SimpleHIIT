package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.di.DefaultDispatcher
import fr.shining_cat.simplehiit.domain.models.*
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BuildSessionUseCase @Inject constructor(
    private val formatLongDurationMsAsSmallestHhMmSsStringUseCase: FormatLongDurationMsAsSmallestHhMmSsStringUseCase,
    private val composeExercisesListForSessionUseCase: ComposeExercisesListForSessionUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val hiitLogger: HiitLogger
) {

    suspend fun execute(
        sessionSettings: SessionSettings,
        durationStringFormatter: DurationStringFormatter
    ): Session {
        return withContext(defaultDispatcher) {
            val totalSessionLengthMs =
                sessionSettings.cycleLengthMs.times(sessionSettings.numberCumulatedCycles)
            val totalSessionLengthFormatted =
                formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                    totalSessionLengthMs,
                    durationStringFormatter
                )
            val exercisesList = getSelectedExercisesTypesList(sessionSettings)
            val steps = buildStepsList(
                exercisesList = exercisesList,
                restPeriodLengthMs = sessionSettings.restPeriodLengthMs,
                workPeriodLengthMs = sessionSettings.workPeriodLengthMs,
                periodsStartCountDownLengthMs = sessionSettings.periodsStartCountDownLengthMs,
                sessionStartCountDownLengthMs = sessionSettings.sessionStartCountDownLengthMs,
                durationStringFormatter = durationStringFormatter
            )
            Session(
                steps = steps,
                durationMs = totalSessionLengthMs,
                durationFormatted = totalSessionLengthFormatted,
                beepSoundCountDownActive = sessionSettings.beepSoundCountDownActive
            )
        }
    }

    private suspend fun getSelectedExercisesTypesList(sessionSettings: SessionSettings): List<Exercise> {
        return withContext(defaultDispatcher) {
            val selectedExerciseTypes =
                sessionSettings.exerciseTypes.filter { it.selected }.map { it.type }
            composeExercisesListForSessionUseCase.execute(
                numberOfWorkPeriodsPerCycle = sessionSettings.numberOfWorkPeriods,
                numberOfCycles = sessionSettings.numberCumulatedCycles,
                selectedExerciseTypes = selectedExerciseTypes
            )
        }
    }

    private suspend fun buildStepsList(
        exercisesList: List<Exercise>,
        restPeriodLengthMs: Long,
        workPeriodLengthMs: Long,
        periodsStartCountDownLengthMs: Long,
        sessionStartCountDownLengthMs: Long,
        durationStringFormatter: DurationStringFormatter
    ): List<SessionStep> {
        return withContext(defaultDispatcher) {
            val allSteps = mutableListOf<SessionStep>()
            val totalSteps = exercisesList.size
            if (sessionStartCountDownLengthMs > 0L) {
                val totalSessionDurationMs =
                    totalSteps.times(workPeriodLengthMs.plus(restPeriodLengthMs))
                val prepareStep = SessionStep.PrepareStep(
                    durationMs = sessionStartCountDownLengthMs,
                    remainingSessionDurationMsAfterMe = totalSessionDurationMs,
                    countDownLengthMs = sessionStartCountDownLengthMs
                )
                allSteps.add(prepareStep)
            }
            for ((index, exercise) in exercisesList.withIndex()) {
                hiitLogger.d("BuildSessionUseCase", "buildStepsList::index = $index")
                //we're not checking the asymmetrical attribute here as it has already been taking into consideration while building this exercisesList by adding asymmetrical exercises twice.
                val stepExerciseSide = when (exercise) {
                    exercisesList.getOrNull(index - 1) -> {//if previous exercise was the same, then we are handling an asymmetrical for the second side
                        AsymmetricalExerciseSideOrder.SECOND.side
                    }
                    exercisesList.getOrNull(index + 1) -> {//if next exercise will be the same, then we are handling an asymmetrical for the first side
                        AsymmetricalExerciseSideOrder.FIRST.side
                    }
                    else -> { // otherwise we are handling a symmetrical exercise
                        ExerciseSide.NONE
                    }
                }
                //
                val remainingExercisesAfterStep = totalSteps.minus(index).minus(1)
                hiitLogger.d(
                    "BuildSessionUseCase",
                    "buildStepsList::remainingExercises = $remainingExercisesAfterStep"
                )
                val restStepDurationFormatted =
                    formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                        restPeriodLengthMs,
                        durationStringFormatter
                    )
                val workStepDurationFormatter =
                    formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                        workPeriodLengthMs,
                        durationStringFormatter
                    )
                val remainingSessionDurationMsAfterRest =
                    (remainingExercisesAfterStep.plus(1)).times(workPeriodLengthMs) + (remainingExercisesAfterStep).times(
                        restPeriodLengthMs
                    )
                hiitLogger.d(
                    "BuildSessionUseCase",
                    "buildStepsList::remainingSessionDurationMsAfterRest = $remainingSessionDurationMsAfterRest"
                )
                val remainingSessionDurationMsAfterWork =
                    remainingExercisesAfterStep.times(workPeriodLengthMs.plus(restPeriodLengthMs))
                hiitLogger.d(
                    "BuildSessionUseCase",
                    "buildStepsList::remainingSessionDurationMsAfterWork = $remainingSessionDurationMsAfterWork"
                )
                //
                val restStep = SessionStep.RestStep(
                    exercise = exercise,
                    side = stepExerciseSide,
                    durationMs = restPeriodLengthMs,
                    durationFormatted = restStepDurationFormatted,
                    remainingSessionDurationMsAfterMe = remainingSessionDurationMsAfterRest,
                    countDownLengthMs = periodsStartCountDownLengthMs
                )
                allSteps.add(restStep)
                //
                val workStep = SessionStep.WorkStep(
                    exercise = exercise,
                    side = stepExerciseSide,
                    durationMs = workPeriodLengthMs,
                    durationFormatted = workStepDurationFormatter,
                    remainingSessionDurationMsAfterMe = remainingSessionDurationMsAfterWork,
                    countDownLengthMs = periodsStartCountDownLengthMs
                )
                allSteps.add(workStep)
            }
            hiitLogger.d("BuildSessionStepsList", "execute::input exercisesList : $exercisesList")
            hiitLogger.d("BuildSessionStepsList", "execute::output stepsList : $allSteps")
            allSteps.toList()
        }
    }

}