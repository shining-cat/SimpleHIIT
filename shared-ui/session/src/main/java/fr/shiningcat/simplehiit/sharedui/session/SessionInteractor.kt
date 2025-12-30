package fr.shiningcat.simplehiit.sharedui.session

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.Session
import fr.shiningcat.simplehiit.domain.common.models.SessionRecord
import fr.shiningcat.simplehiit.domain.common.models.SessionSettings
import fr.shiningcat.simplehiit.domain.common.models.StepTimerState
import fr.shiningcat.simplehiit.domain.common.usecases.DurationFormatStyle
import fr.shiningcat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shiningcat.simplehiit.domain.session.usecases.BuildSessionUseCase
import fr.shiningcat.simplehiit.domain.session.usecases.GetSessionSettingsUseCase
import fr.shiningcat.simplehiit.domain.session.usecases.InsertSessionUseCase
import fr.shiningcat.simplehiit.domain.session.usecases.StepTimerUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SessionInteractor {
    fun getSessionSettings(): Flow<Output<SessionSettings>>

    suspend fun buildSession(sessionSettings: SessionSettings): Session

    fun formatLongDurationMsAsSmallestHhMmSsString(durationMs: Long): String

    suspend fun startStepTimer(totalMilliSeconds: Long)

    fun getStepTimerState(): StateFlow<StepTimerState>

    suspend fun insertSession(sessionRecord: SessionRecord): Output<Int>
}

class SessionInteractorImpl(
    private val getSessionSettingsUseCase: GetSessionSettingsUseCase,
    private val buildSessionUseCase: BuildSessionUseCase,
    private val formatLongDurationMsAsSmallestHhMmSsStringUseCase: FormatLongDurationMsAsSmallestHhMmSsStringUseCase,
    private val stepTimerUseCase: StepTimerUseCase,
    private val insertSessionUseCase: InsertSessionUseCase,
) : SessionInteractor {
    override fun getSessionSettings(): Flow<Output<SessionSettings>> = getSessionSettingsUseCase.execute()

    override suspend fun buildSession(sessionSettings: SessionSettings): Session = buildSessionUseCase.execute(sessionSettings)

    override fun formatLongDurationMsAsSmallestHhMmSsString(durationMs: Long): String =
        formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
            durationMs = durationMs,
            formatStyle = DurationFormatStyle.SHORT,
        )

    override suspend fun startStepTimer(totalMilliSeconds: Long) = stepTimerUseCase.start(totalMilliSeconds)

    override fun getStepTimerState(): StateFlow<StepTimerState> = stepTimerUseCase.timerStateFlow

    override suspend fun insertSession(sessionRecord: SessionRecord): Output<Int> = insertSessionUseCase.execute(sessionRecord)
}
