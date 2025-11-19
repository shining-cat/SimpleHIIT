package fr.shiningcat.simplehiit.android.mobile.ui.common.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import fr.shiningcat.simplehiit.android.common.Screen
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R

/**
 * Side navigation bar displayed in horizontal layout mode.
 *
 * This component uses NavigationRail with [NavigationRailDefaults.windowInsets] to handle
 * window insets natively. This is important for proper edge-to-edge handling:
 *
 * - **Start-side insets:** NavigationRail automatically applies padding for system bars and
 *   display cutouts on the start side, ensuring the navigation bar content is not obscured
 * - **System bar tinting:** The native windowInsets ensure proper tinting of system bars
 *   behind the navigation bar
 * - **Content protection:** Screens using this component should apply their own end-side
 *   cutout padding to protect main content when the device is rotated 180° (see StatisticsScreen
 *   for an example)
 *
 * Do not override the windowInsets parameter unless you have a specific reason and understand
 * the implications for edge-to-edge display and system bar handling.
 *
 * @param navigateTo Callback to navigate to a different screen
 * @param currentDestination The currently displayed screen
 * @param showStatisticsButton Whether to show the statistics navigation button
 */
@Composable
fun NavigationSideBar(
    navigateTo: (String) -> Unit = {},
    currentDestination: Screen,
    showStatisticsButton: Boolean,
) {
    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        header = {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2)),
            )
        },
        windowInsets = NavigationRailDefaults.windowInsets,
    ) {
        SideBarItem(
            onClick = { navigateTo(Screen.Home.route) },
            icon = R.drawable.home,
            label = R.string.home_page_title,
            selected = currentDestination == Screen.Home,
        )
        SideBarItem(
            onClick = { navigateTo(Screen.Settings.route) },
            icon = R.drawable.cog,
            label = R.string.settings_button_content_label,
            selected = currentDestination == Screen.Settings,
        )
        if (showStatisticsButton) {
            SideBarItem(
                onClick = { navigateTo(Screen.Statistics.route) },
                icon = R.drawable.bar_chart,
                label = R.string.statistics_button_content_label,
                selected = currentDestination == Screen.Statistics,
            )
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun NavigationSideBarPreview() {
    SimpleHiitMobileTheme {
        Surface {
            NavigationSideBar(
                currentDestination = Screen.Settings,
                showStatisticsButton = true,
            )
        }
    }
}
