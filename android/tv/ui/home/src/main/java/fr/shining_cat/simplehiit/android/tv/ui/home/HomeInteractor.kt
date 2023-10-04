package fr.shining_cat.simplehiit.android.tv.ui.home

import fr.shining_cat.simplehiit.domain.common.Constants
import fr.shining_cat.simplehiit.domain.common.Output
import fr.shining_cat.simplehiit.domain.common.models.HomeSettings
import fr.shining_cat.simplehiit.domain.common.models.User
import fr.shining_cat.simplehiit.domain.common.usecases.ResetWholeAppUseCase
import fr.shining_cat.simplehiit.domain.home.usecases.GetHomeSettingsUseCase
import fr.shining_cat.simplehiit.domain.home.usecases.SetTotalRepetitionsNumberUseCase
import fr.shining_cat.simplehiit.domain.home.usecases.ToggleUserSelectedUseCase
import fr.shining_cat.simplehiit.domain.home.usecases.ValidateInputNumberCyclesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface HomeInteractor {
    fun getHomeSettings(): Flow<Output<HomeSettings>>
    suspend fun setTotalRepetitionsNumber(number: Int)
    suspend fun toggleUserSelected(user: User): Output<Int>
    suspend fun resetWholeApp()
    fun validateInputNumberCycles(input: String): Constants.InputError
}

class HomeInteractorImpl @Inject constructor(
    private val getHomeSettingsUseCase: GetHomeSettingsUseCase,
    private val setTotalRepetitionsNumberUseCase: SetTotalRepetitionsNumberUseCase,
    private val toggleUserSelectedUseCase: ToggleUserSelectedUseCase,
    private val resetWholeAppUseCase: ResetWholeAppUseCase,
    private val validateInputNumberCyclesUseCase: ValidateInputNumberCyclesUseCase
) : HomeInteractor {

    override fun getHomeSettings(): Flow<Output<HomeSettings>> {
        return getHomeSettingsUseCase.execute()
    }

    override suspend fun setTotalRepetitionsNumber(number: Int) {
        return setTotalRepetitionsNumberUseCase.execute(number)
    }

    override suspend fun toggleUserSelected(user: User): Output<Int> {
        return toggleUserSelectedUseCase.execute(user)
    }

    override suspend fun resetWholeApp() {
        return resetWholeAppUseCase.execute()
    }

    override fun validateInputNumberCycles(input: String): Constants.InputError {
        return validateInputNumberCyclesUseCase.execute(input)
    }

}