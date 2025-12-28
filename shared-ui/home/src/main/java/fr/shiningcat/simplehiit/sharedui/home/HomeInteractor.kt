package fr.shiningcat.simplehiit.sharedui.home

import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.HomeSettings
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.domain.common.usecases.ResetWholeAppUseCase
import fr.shiningcat.simplehiit.domain.home.usecases.GetHomeSettingsUseCase
import fr.shiningcat.simplehiit.domain.home.usecases.SetTotalRepetitionsNumberUseCase
import fr.shiningcat.simplehiit.domain.home.usecases.ToggleUserSelectedUseCase
import fr.shiningcat.simplehiit.domain.home.usecases.ValidateInputNumberCyclesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface HomeInteractor {
    fun getHomeSettings(): Flow<Output<HomeSettings>>

    suspend fun setTotalRepetitionsNumber(number: Int)

    suspend fun toggleUserSelected(user: User): Output<Int>

    suspend fun resetWholeApp()

    fun validateInputNumberCycles(input: String): Constants.InputError
}

class HomeInteractorImpl
    @Inject
    constructor(
        private val getHomeSettingsUseCase: GetHomeSettingsUseCase,
        private val setTotalRepetitionsNumberUseCase: SetTotalRepetitionsNumberUseCase,
        private val toggleUserSelectedUseCase: ToggleUserSelectedUseCase,
        private val resetWholeAppUseCase: ResetWholeAppUseCase,
        private val validateInputNumberCyclesUseCase: ValidateInputNumberCyclesUseCase,
    ) : HomeInteractor {
        override fun getHomeSettings(): Flow<Output<HomeSettings>> = getHomeSettingsUseCase.execute()

        override suspend fun setTotalRepetitionsNumber(number: Int) = setTotalRepetitionsNumberUseCase.execute(number)

        override suspend fun toggleUserSelected(user: User): Output<Int> = toggleUserSelectedUseCase.execute(user)

        override suspend fun resetWholeApp() = resetWholeAppUseCase.execute()

        override fun validateInputNumberCycles(input: String): Constants.InputError = validateInputNumberCyclesUseCase.execute(input)
    }
