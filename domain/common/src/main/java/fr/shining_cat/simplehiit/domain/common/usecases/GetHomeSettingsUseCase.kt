package fr.shining_cat.simplehiit.domain.common.usecases

import fr.shining_cat.simplehiit.domain.common.Constants
import fr.shining_cat.simplehiit.domain.common.Output
import fr.shining_cat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.common.models.HomeSettings
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import javax.inject.Inject

class GetHomeSettingsUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val simpleHiitLogger: HiitLogger
) {

    fun execute(): Flow<Output<HomeSettings>> {
        val usersFlow = simpleHiitRepository.getUsers()
        val settingsFlow = simpleHiitRepository.getPreferences()
        return usersFlow.combineTransform(settingsFlow) { usersOutput, settings ->
            if (usersOutput is Output.Error) {
                val exception = usersOutput.exception
                simpleHiitLogger.e("GetHomeSettingsUseCase", "Error retrieving users", exception)
                emit(Output.Error(Constants.Errors.NO_USERS_FOUND, exception))
            } else {
                usersOutput as Output.Success
                val totalCycleLength = settings.numberOfWorkPeriods.times(
                    (settings.workPeriodLengthMs.plus(settings.restPeriodLengthMs))
                )
                emit(
                    Output.Success(
                        HomeSettings(
                            numberCumulatedCycles = settings.numberCumulatedCycles,
                            cycleLengthMs = totalCycleLength,
                            users = usersOutput.result
                        )
                    )
                )
            }
        }
    }
}