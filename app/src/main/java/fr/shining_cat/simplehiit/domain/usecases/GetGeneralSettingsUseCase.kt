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
                            workPeriodLengthSeconds = settings.workPeriodLength,
                            restPeriodLengthSeconds = settings.restPeriodLength,
                            numberOfWorkPeriods = settings.numberOfWorkPeriods,
                            beepSoundCountDownActive = settings.beepSoundActive,
                            sessionStartCountDownLengthSeconds = settings.sessionCountDownLengthSeconds,
                            periodsStartCountDownLengthSeconds = settings.PeriodCountDownLengthSeconds,
                            users = usersOutput.result,
                            exerciseTypes = settings.selectedExercisesTypes
                        )
                    )
                )
            }
        }
    }
}