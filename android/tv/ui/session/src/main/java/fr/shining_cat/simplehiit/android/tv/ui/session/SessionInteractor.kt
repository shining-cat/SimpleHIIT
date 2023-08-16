package fr.shining_cat.simplehiit.android.tv.ui.session

import fr.shining_cat.simplehiit.domain.common.Output
import fr.shining_cat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shining_cat.simplehiit.domain.common.models.Session
import fr.shining_cat.simplehiit.domain.common.models.SessionRecord
import fr.shining_cat.simplehiit.domain.common.models.SessionSettings
import fr.shining_cat.simplehiit.domain.common.models.StepTimerState
import fr.shining_cat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shining_cat.simplehiit.domain.session.usecases.BuildSessionUseCase
import fr.shining_cat.simplehiit.domain.session.usecases.GetSessionSettingsUseCase
import fr.shining_cat.simplehiit.domain.session.usecases.InsertSessionUseCase
import fr.shining_cat.simplehiit.domain.session.usecases.StepTimerUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface SessionInteractor {
    fun getSessionSettings(): Flow<Output<SessionSettings>>
    suspend fun buildSession(
        sessionSettings: SessionSettings,
        durationStringFormatter: DurationStringFormatter
    ): Session

    fun formatLongDurationMsAsSmallestHhMmSsString(
        durationMs: Long,
        durationStringFormatter: DurationStringFormatter
    ): String

    suspend fun startStepTimer(totalMilliSeconds: Long)
    fun getStepTimerState(): StateFlow<StepTimerState>
    suspend fun insertSession(sessionRecord: SessionRecord): Output<Int>
}

class SessionInteractorImpl @Inject constructor(
    private val getSessionSettingsUseCase: GetSessionSettingsUseCase,
    private val buildSessionUseCase: BuildSessionUseCase,
    private val formatLongDurationMsAsSmallestHhMmSsStringUseCase: FormatLongDurationMsAsSmallestHhMmSsStringUseCase,
    private val stepTimerUseCase: StepTimerUseCase,
    private val insertSessionUseCase: InsertSessionUseCase
) : SessionInteractor {

    override fun getSessionSettings(): Flow<Output<SessionSettings>> {
        return getSessionSettingsUseCase.execute()
    }

    override suspend fun buildSession(
        sessionSettings: SessionSettings,
        durationStringFormatter: DurationStringFormatter
    ): Session {
        return buildSessionUseCase.execute(sessionSettings, durationStringFormatter)
    }

    override fun formatLongDurationMsAsSmallestHhMmSsString(
        durationMs: Long,
        durationStringFormatter: DurationStringFormatter
    ): String {
        return formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
            durationMs,
            durationStringFormatter
        )
    }

    override suspend fun startStepTimer(totalMilliSeconds: Long) {
        return stepTimerUseCase.start(totalMilliSeconds)

    }

    override fun getStepTimerState(): StateFlow<StepTimerState> {
        return stepTimerUseCase.timerStateFlow
    }

    override suspend fun insertSession(sessionRecord: SessionRecord): Output<Int> {
        return insertSessionUseCase.execute(sessionRecord)
    }

}