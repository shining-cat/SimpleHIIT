package fr.shiningcat.simplehiit.android.mobile.app

import androidx.compose.runtime.Composable
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
    NavDisplay(
        backStack = navigationViewModel.backStack,
        onBack = { navigationViewModel.goBack() },
        entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator()),
        entryProvider =
            entryProvider {
                entry<Screen.Home> {
                    HomeScreen(
                        navigateTo = navigationViewModel::navigateTo,
                        uiArrangement = uiArrangement,
                        hiitLogger = hiitLogger,
                    )
                }
                entry<Screen.Settings> {
                    SettingsScreen(
                        navigateTo = navigationViewModel::navigateTo,
                        uiArrangement = uiArrangement,
                        hiitLogger = hiitLogger,
                    )
                }
                entry<Screen.Statistics> {
                    StatisticsScreen(
                        navigateTo = navigationViewModel::navigateTo,
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
                        navigateTo = navigationViewModel::navigateTo,
                        uiArrangement = uiArrangement,
                    )
                }
            },
    )
}
