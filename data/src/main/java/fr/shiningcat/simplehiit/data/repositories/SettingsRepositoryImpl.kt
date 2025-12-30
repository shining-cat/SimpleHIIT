package fr.shiningcat.simplehiit.data.repositories

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.SimpleHiitPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SettingsRepositoryImpl(
    private val simpleHiitDataStoreManager: SimpleHiitDataStoreManager,
    private val ioDispatcher: CoroutineDispatcher,
    private val hiitLogger: HiitLogger,
) : SettingsRepository {
    override fun getPreferences(): Flow<SimpleHiitPreferences> =
        runCatching {
            simpleHiitDataStoreManager.getPreferences()
        }.getOrElse { exception ->
            hiitLogger.e(
                "SettingsRepositoryImpl",
                "failed getting general settings - returning default settings",
                exception,
            )
            flowOf(SimpleHiitPreferences())
        }

    override suspend fun setWorkPeriodLength(durationMs: Long) {
        simpleHiitDataStoreManager.setWorkPeriodLength(durationMs)
    }

    override suspend fun setRestPeriodLength(durationMs: Long) {
        simpleHiitDataStoreManager.setRestPeriodLength(durationMs)
    }

    override suspend fun setNumberOfWorkPeriods(number: Int) {
        simpleHiitDataStoreManager.setNumberOfWorkPeriods(number)
    }

    override suspend fun setBeepSound(active: Boolean) {
        simpleHiitDataStoreManager.setBeepSound(active)
    }

    override suspend fun setSessionStartCountdown(durationMs: Long) {
        simpleHiitDataStoreManager.setSessionStartCountdown(durationMs)
    }

    override suspend fun setPeriodStartCountdown(durationMs: Long) {
        simpleHiitDataStoreManager.setPeriodStartCountdown(durationMs)
    }

    override suspend fun setTotalRepetitionsNumber(number: Int) {
        simpleHiitDataStoreManager.setNumberOfCumulatedCycles(number = number)
    }

    override suspend fun setExercisesTypesSelected(exercisesTypes: List<ExerciseType>) {
        simpleHiitDataStoreManager.setExercisesTypesSelected(exercisesTypes = exercisesTypes)
    }

    override suspend fun setAppTheme(theme: AppTheme) {
        simpleHiitDataStoreManager.setAppTheme(theme = theme)
    }

    override suspend fun resetAllSettings() {
        simpleHiitDataStoreManager.clearAll()
    }
}
