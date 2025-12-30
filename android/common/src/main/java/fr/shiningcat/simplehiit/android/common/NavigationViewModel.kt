package fr.shiningcat.simplehiit.android.common

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import fr.shiningcat.simplehiit.commonutils.HiitLogger

/**
 * Centralized ViewModel for managing navigation state across the app.
 * Provides a single source of truth for the navigation back stack.
 *
 * @param hiitLogger Logger for tracking navigation events
 */
class NavigationViewModel(
    private val hiitLogger: HiitLogger,
) : ViewModel() {
    private val _backStack: SnapshotStateList<Screen> = mutableListOf<Screen>(Screen.Home).toMutableStateList()

    /**
     * The current navigation back stack.
     * Exposed as read-only to prevent direct manipulation outside controlled methods.
     */
    val backStack: SnapshotStateList<Screen> = _backStack

    /**
     * Navigate to a new destination by adding it to the back stack.
     *
     * @param destination The screen to navigate to
     */
    fun navigateTo(destination: Screen) {
        hiitLogger.d(TAG, "navigateTo: $destination")
        _backStack.add(destination)
    }

    /**
     * Navigate back by removing the last entry from the back stack.
     *
     * @return true if navigation was successful, false if already at root
     */
    fun goBack(): Boolean =
        if (_backStack.size > 1) {
            val removed = _backStack.removeLastOrNull()
            hiitLogger.d(TAG, "goBack: removed $removed, remaining stack size: ${_backStack.size}")
            true
        } else {
            hiitLogger.d(TAG, "goBack: already at root, cannot go back")
            false
        }

    /**
     * Clear the entire back stack and navigate to a new destination.
     * Useful for reset operations or switching to a completely new flow.
     *
     * @param destination The screen to navigate to after clearing
     */
    fun clearAndNavigateTo(destination: Screen) {
        hiitLogger.d(TAG, "clearAndNavigateTo: clearing stack and navigating to $destination")
        _backStack.clear()
        _backStack.add(destination)
    }

    private companion object {
        private const val TAG = "NavigationViewModel"
    }
}
