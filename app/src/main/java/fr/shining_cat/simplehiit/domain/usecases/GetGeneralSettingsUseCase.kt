package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.GeneralSettings
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import javax.inject.Inject

class GetGeneralSettingsUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val simpleHiitLogger: HiitLogger
) {

    fun execute(): Flow<Output<GeneralSettings>> {
        val usersFlow = simpleHiitRepository.getUsers()
        val settingsFlow = simpleHiitRepository.getPreferences()
        return usersFlow.combineTransform(settingsFlow) { usersOutput, settings ->
            if (usersOutput is Output.Error) {
                val exception = usersOutput.exception
                simpleHiitLogger.e("GetGeneralSettingsUseCase", "Error retrieving users", exception)
                emit(Output.Error(Constants.Errors.NO_USERS_FOUND, exception))
            } else {
                usersOutput as Output.Success
                emit(
                    Output.Success(
                        GeneralSettings(
                            workPeriodLengthMs = settings.workPeriodLengthMs,
                            restPeriodLengthMs = settings.restPeriodLengthMs,
                            numberOfWorkPeriods = settings.numberOfWorkPeriods,
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