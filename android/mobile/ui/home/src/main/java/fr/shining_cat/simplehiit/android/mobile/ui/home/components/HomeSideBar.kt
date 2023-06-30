package fr.shining_cat.simplehiit.android.mobile.ui.home.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.android.mobile.ui.common.Screen
import fr.shining_cat.simplehiit.android.mobile.ui.common.components.SideBarItem
import fr.shining_cat.simplehiit.android.mobile.ui.home.HomeViewState
import fr.shining_cat.simplehiit.commonresources.R

@Composable
fun HomeSideBar(
    navigateTo: (String) -> Unit = {},
    screenViewState: HomeViewState
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
            onClick = { navigateTo(Screen.Statistics.route) },
            icon = R.drawable.home,
            label = R.string.home_page_title,
            selected = true
        )
        SideBarItem(
            onClick = { navigateTo(Screen.Settings.route) },
            icon = R.drawable.cog,
            label = R.string.settings_button_content_label
        )
        if (screenViewState is HomeViewState.Nominal) {
            SideBarItem(
                onClick = { navigateTo(Screen.Statistics.route) },
                icon = R.drawable.bar_chart,
                label = R.string.statistics_button_content_label
            )
        }
    }
}