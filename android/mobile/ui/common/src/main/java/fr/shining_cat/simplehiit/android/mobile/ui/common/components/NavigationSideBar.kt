package fr.shining_cat.simplehiit.android.mobile.ui.common.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.android.common.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.commonresources.R

@Composable
fun NavigationSideBar(
    navigateTo: (String) -> Unit = {},
    currentDestination: fr.shining_cat.simplehiit.android.common.Screen,
    showStatisticsButton: Boolean
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
                modifier = Modifier.padding(16.dp)
            )
        }
    ) {
        SideBarItem(
            onClick = { navigateTo(fr.shining_cat.simplehiit.android.common.Screen.Home.route) },
            icon = R.drawable.home,
            label = R.string.home_page_title,
            selected = currentDestination == fr.shining_cat.simplehiit.android.common.Screen.Home
        )
        SideBarItem(
            onClick = { navigateTo(fr.shining_cat.simplehiit.android.common.Screen.Settings.route) },
            icon = R.drawable.cog,
            label = R.string.settings_button_content_label,
            selected = currentDestination == fr.shining_cat.simplehiit.android.common.Screen.Settings
        )
        if (showStatisticsButton) {
            SideBarItem(
                onClick = { navigateTo(fr.shining_cat.simplehiit.android.common.Screen.Statistics.route) },
                icon = R.drawable.bar_chart,
                label = R.string.statistics_button_content_label,
                selected = currentDestination == fr.shining_cat.simplehiit.android.common.Screen.Statistics
            )
        }
    }
}

// Previews
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun NavigationSideBarPreview() {
    SimpleHiitTheme {
        Surface {
            NavigationSideBar(
                currentDestination = fr.shining_cat.simplehiit.android.common.Screen.Settings,
                showStatisticsButton = true
            )
        }
    }
}