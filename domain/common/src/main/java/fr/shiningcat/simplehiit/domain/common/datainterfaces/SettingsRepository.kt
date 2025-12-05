package fr.shiningcat.simplehiit.domain.common.datainterfaces

import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.SimpleHiitPreferences
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getPreferences(): Flow<SimpleHiitPreferences>

    suspend fun setWorkPeriodLength(durationMs: Long)

    suspend fun setRestPeriodLength(durationMs: Long)

    suspend fun setNumberOfWorkPeriods(number: Int)

    suspend fun setBeepSound(active: Boolean)

    suspend fun setSessionStartCountdown(durationMs: Long)

    suspend fun setPeriodStartCountdown(durationMs: Long)

    suspend fun setTotalRepetitionsNumber(number: Int)

    suspend fun setExercisesTypesSelected(exercisesTypes: List<ExerciseType>)

    suspend fun setAppTheme(theme: AppTheme)

    suspend fun resetAllSettings()
}
