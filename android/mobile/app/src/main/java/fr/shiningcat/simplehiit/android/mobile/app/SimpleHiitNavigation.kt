/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.app

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import fr.shiningcat.simplehiit.android.mobile.ui.common.AboutScreen
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.home.HomeScreen
import fr.shiningcat.simplehiit.android.mobile.ui.session.SessionScreen
import fr.shiningcat.simplehiit.android.mobile.ui.settings.SettingsScreen
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.StatisticsScreen
import fr.shiningcat.simplehiit.android.shared.core.NavigationViewModel
import fr.shiningcat.simplehiit.android.shared.core.Screen
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import org.koin.androidx.compose.koinViewModel

@Composable
fun SimpleHiitNavigation(
    uiArrangement: UiArrangement,
    hiitLogger: HiitLogger,
    navigationViewModel: NavigationViewModel = koinViewModel(),
) {
    // Top-level destinations (reachable from the navigation sidebar) reset the back stack:
    // switching between them is flat navigation, not a push. Session is a flow entered from
    // Home, so it is pushed onto the stack and can be backed out of.
    val navigate: (Screen) -> Unit = { destination ->
        when (destination) {
            Screen.Home, Screen.Settings, Screen.Statistics, Screen.About ->
                navigationViewModel.clearAndNavigateTo(destination)
            Screen.Session ->
                navigationViewModel.navigateTo(destination)
        }
    }
    NavDisplay(
        backStack = navigationViewModel.backStack,
        onBack = { navigationViewModel.goBack() },
        entryDecorators =
            listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
        entryProvider =
            entryProvider {
                entry<Screen.Home> {
                    HomeScreen(
                        navigateTo = navigate,
                        uiArrangement = uiArrangement,
                        hiitLogger = hiitLogger,
                    )
                }
                entry<Screen.Settings> {
                    SettingsScreen(
                        navigateTo = navigate,
                        uiArrangement = uiArrangement,
                        hiitLogger = hiitLogger,
                    )
                }
                entry<Screen.Statistics> {
                    StatisticsScreen(
                        navigateTo = navigate,
                        uiArrangement = uiArrangement,
                        hiitLogger = hiitLogger,
                    )
                }
                entry<Screen.Session> {
                    SessionScreen(
                        navigateUp = navigationViewModel::goBack,
                        uiArrangement = uiArrangement,
                        hiitLogger = hiitLogger,
                    )
                }
                entry<Screen.About> {
                    AboutScreen(
                        navigateTo = navigate,
                        uiArrangement = uiArrangement,
                    )
                }
            },
    )
}
