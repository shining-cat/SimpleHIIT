package fr.shiningcat.simplehiit.data.repositories

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.data.di.IoDispatcher
import fr.shiningcat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.SimpleHiitPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsRepositoryImpl
    @Inject
    constructor(
        private val hiitDataStoreManager: SimpleHiitDataStoreManager,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
        private val hiitLogger: HiitLogger,
    ) : SettingsRepository {
        override fun getPreferences(): Flow<SimpleHiitPreferences> =
            runCatching {
                hiitDataStoreManager.getPreferences()
            }.getOrElse { exception ->
                hiitLogger.e(
                    "SettingsRepositoryImpl",
                    "failed getting general settings - returning default settings",
                    exception,
                )
                flowOf(SimpleHiitPreferences())
            }

        override suspend fun setWorkPeriodLength(durationMs: Long) {
            withContext(ioDispatcher) {
                hiitDataStoreManager.setWorkPeriodLength(durationMs)
            }
        }

        override suspend fun setRestPeriodLength(durationMs: Long) {
            withContext(ioDispatcher) {
                hiitDataStoreManager.setRestPeriodLength(durationMs)
            }
        }

        override suspend fun setNumberOfWorkPeriods(number: Int) {
            withContext(ioDispatcher) {
                hiitDataStoreManager.setNumberOfWorkPeriods(number)
            }
        }

        override suspend fun setBeepSound(active: Boolean) {
            withContext(ioDispatcher) {
                hiitDataStoreManager.setBeepSound(active)
            }
        }

        override suspend fun setSessionStartCountdown(durationMs: Long) {
            withContext(ioDispatcher) {
                hiitDataStoreManager.setSessionStartCountdown(durationMs)
            }
        }

        override suspend fun setPeriodStartCountdown(durationMs: Long) {
            withContext(ioDispatcher) {
                hiitDataStoreManager.setPeriodStartCountdown(durationMs)
            }
        }

        override suspend fun setTotalRepetitionsNumber(number: Int) {
            withContext(ioDispatcher) {
                hiitDataStoreManager.setNumberOfCumulatedCycles(number = number)
            }
        }

        override suspend fun setExercisesTypesSelected(exercisesTypes: List<ExerciseType>) {
            withContext(ioDispatcher) {
                hiitDataStoreManager.setExercisesTypesSelected(exercisesTypes = exercisesTypes)
            }
        }

        override suspend fun setAppTheme(theme: AppTheme) {
            withContext(ioDispatcher) {
                hiitDataStoreManager.setAppTheme(theme = theme)
            }
        }

        override suspend fun resetAllSettings() {
            withContext(ioDispatcher) {
                hiitDataStoreManager.clearAll()
            }
        }
    }
