package fr.shiningcat.simplehiit.android.tv.ui.settings

import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.GeneralSettings
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.domain.settings.usecases.CreateUserUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.DeleteUserUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.GetGeneralSettingsUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ResetAllSettingsUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SaveSelectedExerciseTypesUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetAppLanguageUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetBeepSoundUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetNumberOfWorkPeriodsUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetPeriodStartCountDownUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetRestPeriodLengthUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetSessionStartCountDownUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetWorkPeriodLengthUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ToggleExerciseTypeInListUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.UpdateUserNameUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ValidateInputPeriodStartCountdownUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ValidateInputSessionStartCountdownUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ValidateInputUserNameUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ValidateNumberOfWorkPeriodsUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ValidatePeriodLengthUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SettingsInteractor {
    fun getGeneralSettings(): Flow<Output<GeneralSettings>>

    suspend fun setWorkPeriodLength(durationMs: Long)

    suspend fun setRestPeriodLength(durationMs: Long)

    suspend fun setNumberOfWorkPeriods(number: Int)

    suspend fun setBeepSound(active: Boolean)

    suspend fun setSessionStartCountDown(durationMs: Long)

    suspend fun setPeriodStartCountDown(durationMs: Long)

    suspend fun updateUserName(user: User): Output<Int>

    suspend fun deleteUser(user: User): Output<Int>

    suspend fun createUser(user: User): Output<Long>

    suspend fun saveSelectedExerciseTypes(listOfSelectedExerciseTypes: List<ExerciseTypeSelected>)

    suspend fun setAppLanguage(language: AppLanguage): Output<Int>

    suspend fun resetAllSettings()

    fun validatePeriodLength(
        input: String,
        periodCountDownLengthSeconds: Long,
    ): Constants.InputError

    fun validateNumberOfWorkPeriods(input: String): Constants.InputError

    fun validateInputSessionStartCountdown(input: String): Constants.InputError

    fun validateInputPeriodStartCountdown(
        input: String,
        workPeriodLengthSeconds: Long,
        restPeriodLengthSeconds: Long,
    ): Constants.InputError

    fun validateInputUserName(
        user: User,
        existingUsers: List<User>,
    ): Constants.InputError

    fun toggleExerciseTypeInList(
        currentList: List<ExerciseTypeSelected>,
        exerciseTypeToToggle: ExerciseTypeSelected,
    ): List<ExerciseTypeSelected>
}

class SettingsInteractorImpl
    @Inject
    constructor(
        private val getGeneralSettingsUseCase: GetGeneralSettingsUseCase,
        private val setWorkPeriodLengthUseCase: SetWorkPeriodLengthUseCase,
        private val setRestPeriodLengthUseCase: SetRestPeriodLengthUseCase,
        private val setNumberOfWorkPeriodsUseCase: SetNumberOfWorkPeriodsUseCase,
        private val setBeepSoundUseCase: SetBeepSoundUseCase,
        private val setSessionStartCountDownUseCase: SetSessionStartCountDownUseCase,
        private val setPeriodStartCountDownUseCase: SetPeriodStartCountDownUseCase,
        private val updateUserNameUseCase: UpdateUserNameUseCase,
        private val deleteUserUseCase: DeleteUserUseCase,
        private val createUserUseCase: CreateUserUseCase,
        private val saveSelectedExerciseTypesUseCase: SaveSelectedExerciseTypesUseCase,
        private val setAppLanguageUseCase: SetAppLanguageUseCase,
        private val resetAllSettingsUseCase: ResetAllSettingsUseCase,
        private val validatePeriodLengthUseCase: ValidatePeriodLengthUseCase,
        private val validateNumberOfWorkPeriodsUseCase: ValidateNumberOfWorkPeriodsUseCase,
        private val validateInputSessionStartCountdownUseCase: ValidateInputSessionStartCountdownUseCase,
        private val validateInputPeriodStartCountdownUseCase: ValidateInputPeriodStartCountdownUseCase,
        private val validateInputUserNameUseCase: ValidateInputUserNameUseCase,
        private val toggleExerciseTypeInListUseCase: ToggleExerciseTypeInListUseCase,
    ) : SettingsInteractor {
        override fun getGeneralSettings(): Flow<Output<GeneralSettings>> = getGeneralSettingsUseCase.execute()

        override suspend fun setWorkPeriodLength(durationMs: Long) = setWorkPeriodLengthUseCase.execute(durationMs)

        override suspend fun setRestPeriodLength(durationMs: Long) = setRestPeriodLengthUseCase.execute(durationMs)

        override suspend fun setNumberOfWorkPeriods(number: Int) = setNumberOfWorkPeriodsUseCase.execute(number)

        override suspend fun setBeepSound(active: Boolean) = setBeepSoundUseCase.execute(active)

        override suspend fun setSessionStartCountDown(durationMs: Long) = setSessionStartCountDownUseCase.execute(durationMs)

        override suspend fun setPeriodStartCountDown(durationMs: Long) = setPeriodStartCountDownUseCase.execute(durationMs)

        override suspend fun updateUserName(user: User): Output<Int> = updateUserNameUseCase.execute(user)

        override suspend fun deleteUser(user: User): Output<Int> = deleteUserUseCase.execute(user)

        override suspend fun createUser(user: User): Output<Long> = createUserUseCase.execute(user)

        override suspend fun saveSelectedExerciseTypes(listOfSelectedExerciseTypes: List<ExerciseTypeSelected>) =
            saveSelectedExerciseTypesUseCase.execute(listOfSelectedExerciseTypes)

        override suspend fun setAppLanguage(language: AppLanguage) = setAppLanguageUseCase.execute(language)

        override suspend fun resetAllSettings() = resetAllSettingsUseCase.execute()

        override fun validatePeriodLength(
            input: String,
            periodCountDownLengthSeconds: Long,
        ): Constants.InputError = validatePeriodLengthUseCase.execute(input, periodCountDownLengthSeconds)

        override fun validateNumberOfWorkPeriods(input: String): Constants.InputError = validateNumberOfWorkPeriodsUseCase.execute(input)

        override fun validateInputSessionStartCountdown(input: String): Constants.InputError =
            validateInputSessionStartCountdownUseCase.execute(input)

        override fun validateInputPeriodStartCountdown(
            input: String,
            workPeriodLengthSeconds: Long,
            restPeriodLengthSeconds: Long,
        ): Constants.InputError =
            validateInputPeriodStartCountdownUseCase.execute(
                input,
                workPeriodLengthSeconds,
                restPeriodLengthSeconds,
            )

        override fun validateInputUserName(
            user: User,
            existingUsers: List<User>,
        ): Constants.InputError = validateInputUserNameUseCase.execute(user, existingUsers)

        override fun toggleExerciseTypeInList(
            currentList: List<ExerciseTypeSelected>,
            exerciseTypeToToggle: ExerciseTypeSelected,
        ): List<ExerciseTypeSelected> = toggleExerciseTypeInListUseCase.execute(currentList, exerciseTypeToToggle)
    }
