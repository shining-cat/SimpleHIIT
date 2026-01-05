package fr.shiningcat.simplehiit.sharedui.home

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.CyclesModification
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.domain.home.usecases.ModifyNumberCyclesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * Pure Kotlin presenter containing all business logic for the Home screen.
 * No Android framework dependencies - fully unit testable.
 */
class HomePresenter(
    private val homeInteractor: HomeInteractor,
    private val homeViewStateMapper: HomeViewStateMapper,
    private val modifyNumberCyclesUseCase: ModifyNumberCyclesUseCase,
    private val logger: HiitLogger,
) {
    private val _dialogState = MutableStateFlow<HomeDialog>(HomeDialog.None)

    /**
     * The screen view state transforming domain data to UI state.
     */
    val screenViewState: Flow<HomeViewState> =
        homeInteractor
            .getHomeSettings()
            .map { homeViewStateMapper.map(it) }

    /**
     * The dialog state.
     */
    val dialogState: Flow<HomeDialog> = _dialogState.asStateFlow()

    /**
     * Modifies the number of cycles (increase or decrease).
     * Validates that the current state allows this operation and that the new value would be valid.
     *
     * @param currentViewState The current screen state - needed to validate operation is allowed
     * @param modification Whether to increase or decrease
     */
    suspend fun modifyNumberCycles(
        currentViewState: HomeViewState,
        modification: CyclesModification,
    ) {
        if (currentViewState !is HomeViewState.Nominal) {
            logger.e(
                "HomePresenter",
                "modifyNumberCycles called in non-Nominal state: ${currentViewState::class.simpleName}",
            )
            return
        }

        when (
            val result =
                modifyNumberCyclesUseCase.execute(
                    currentValue = currentViewState.numberCumulatedCycles,
                    modification = modification,
                )
        ) {
            is ModifyNumberCyclesUseCase.Result.Success -> {
                homeInteractor.setTotalRepetitionsNumber(result.newValue)
            }
            is ModifyNumberCyclesUseCase.Result.Error.WouldBeNonPositive -> {
                logger.e(
                    "HomePresenter",
                    "Cannot modify cycles - would result in non-positive value",
                )
            }
        }
    }

    /**
     * Toggles the selection state of a user.
     *
     * @param user The user to toggle
     */
    suspend fun toggleUserSelection(user: User) {
        logger.d(
            "HomePresenter",
            "Toggling user: ${user.name}, selected: ${user.selected} -> ${!user.selected}",
        )
        val toggledUser = user.copy(selected = !user.selected)
        homeInteractor.toggleUserSelected(toggledUser)
    }

    /**
     * Shows the confirmation dialog for resetting the whole app.
     */
    suspend fun showResetConfirmation() {
        _dialogState.emit(HomeDialog.ConfirmWholeReset)
    }

    /**
     * Executes the whole app reset after confirmation.
     */
    suspend fun resetWholeApp() {
        homeInteractor.resetWholeApp()
    }

    /**
     * Dismisses any currently shown dialog.
     */
    suspend fun dismissDialog() {
        _dialogState.emit(HomeDialog.None)
    }
}
