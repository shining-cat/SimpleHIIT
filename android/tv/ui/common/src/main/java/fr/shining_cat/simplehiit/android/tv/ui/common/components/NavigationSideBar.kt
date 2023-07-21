package fr.shining_cat.simplehiit.android.tv.ui.common.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shining_cat.simplehiit.android.common.Screen
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.commonresources.R

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun NavigationSideBar(
    navigateTo: (String) -> Unit = {},
    currentDestination: Screen,
    showStatisticsButton: Boolean,
    drawerValue: DrawerValue
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.primary)
    ) {

        Text(
            text = stringResource(R.string.app_name),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        SideBarItem(
            onClick = { navigateTo(Screen.Home.route) },
            icon = R.drawable.home,
            label = R.string.home_page_title,
            selected = currentDestination == Screen.Home,
            drawerValue = drawerValue
        )
        SideBarItem(
            onClick = { navigateTo(Screen.Settings.route) },
            icon = R.drawable.cog,
            label = R.string.settings_button_content_label,
            selected = currentDestination == Screen.Settings,
            drawerValue = drawerValue
        )

        if (showStatisticsButton) {
            SideBarItem(
                onClick = { navigateTo(Screen.Statistics.route) },
                icon = R.drawable.bar_chart,
                label = R.string.statistics_button_content_label,
                selected = currentDestination == Screen.Statistics,
                drawerValue = drawerValue
            )
        }
    }
}

// Previews
@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun NavigationSideBarPreview() {
    SimpleHiitTvTheme {
        Surface {
            NavigationSideBar(
                currentDestination = Screen.Settings,
                showStatisticsButton = true,
                drawerValue = DrawerValue.Open
            )
        }
    }
}