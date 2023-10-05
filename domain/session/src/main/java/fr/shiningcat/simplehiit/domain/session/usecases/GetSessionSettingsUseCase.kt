package fr.shiningcat.simplehiit.domain.session.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shiningcat.simplehiit.domain.common.models.SessionSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import javax.inject.Inject

class GetSessionSettingsUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val simpleHiitLogger: HiitLogger
) {

    fun execute(): Flow<Output<SessionSettings>> {
        val usersFlow = simpleHiitRepository.getSelectedUsers()
        val settingsFlow = simpleHiitRepository.getPreferences()
        return usersFlow.combineTransform(settingsFlow) { usersOutput, settings ->
            if (usersOutput is Output.Error) {
                val exception = usersOutput.exception
                simpleHiitLogger.e("GetGeneralSettingsUseCase", "Error retrieving users", exception)
                emit(Output.Error(Constants.Errors.NO_USERS_FOUND, exception))
            } else {
                usersOutput as Output.Success
                val totalCycleLength =
                    (settings.workPeriodLengthMs + settings.restPeriodLengthMs) * settings.numberOfWorkPeriods
                emit(
                    Output.Success(
                        SessionSettings(
                            numberCumulatedCycles = settings.numberCumulatedCycles,
                            workPeriodLengthMs = settings.workPeriodLengthMs,
                            restPeriodLengthMs = settings.restPeriodLengthMs,
                            numberOfWorkPeriods = settings.numberOfWorkPeriods,
                            cycleLengthMs = totalCycleLength,
                            beepSoundCountDownActive = settings.beepSoundActive,
                            sessionStartCountDownLengthMs = settings.sessionCountDownLengthMs,
                            periodsStartCountDownLengthMs = settings.PeriodCountDownLengthMs,
                            users = usersOutput.result,
                            exerciseTypes = settings.selectedExercisesTypes
                        )
                    )
                )
            }
        }
    }
}