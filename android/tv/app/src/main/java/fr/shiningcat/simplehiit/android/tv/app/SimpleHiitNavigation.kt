package fr.shiningcat.simplehiit.android.tv.app

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.tv.material3.ExperimentalTvMaterial3Api
import fr.shiningcat.simplehiit.android.common.NavigationViewModel
import fr.shiningcat.simplehiit.android.common.Screen
import fr.shiningcat.simplehiit.android.tv.ui.home.HomeScreen
import fr.shiningcat.simplehiit.android.tv.ui.session.SessionScreen
import fr.shiningcat.simplehiit.android.tv.ui.settings.SettingsScreen
import fr.shiningcat.simplehiit.android.tv.ui.statistics.StatisticsScreen
import fr.shiningcat.simplehiit.commonutils.HiitLogger

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SimpleHiitNavigation(
    hiitLogger: HiitLogger,
    navigationViewModel: NavigationViewModel = hiltViewModel(),
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
                        hiitLogger = hiitLogger,
                    )
                }
                entry<Screen.Settings> {
                    SettingsScreen(
                        navigateTo = navigationViewModel::navigateTo,
                        hiitLogger = hiitLogger,
                    )
                }
                entry<Screen.Statistics> {
                    StatisticsScreen(
                        navigateTo = navigationViewModel::navigateTo,
                        hiitLogger = hiitLogger,
                    )
                }
                entry<Screen.Session> {
                    SessionScreen(
                        navigateUp = navigationViewModel::goBack,
                        hiitLogger = hiitLogger,
                    )
                }
            },
    )
}
