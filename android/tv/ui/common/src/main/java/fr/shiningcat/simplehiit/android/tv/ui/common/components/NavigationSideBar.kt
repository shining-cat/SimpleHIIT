package fr.shiningcat.simplehiit.android.tv.ui.common.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.Screen
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun NavigationSideBar(
    navigateTo: (String) -> Unit = {},
    currentDestination: Screen,
    showStatisticsButton: Boolean,
) {
    Column(
        modifier =
            Modifier
                .fillMaxHeight()
                .width(160.dp)
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
    ) {
        Text(
            text = stringResource(R.string.app_name),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(32.dp))

        SideBarItem(
            onClick = { navigateTo(Screen.Home.route) },
            icon = R.drawable.home,
            label = R.string.home_page_title,
            selected = currentDestination == Screen.Home,
        )
        Spacer(modifier = Modifier.height(16.dp))

        SideBarItem(
            onClick = { navigateTo(Screen.Settings.route) },
            icon = R.drawable.cog,
            label = R.string.settings_button_content_label,
            selected = currentDestination == Screen.Settings,
        )

        Spacer(modifier = Modifier.height(16.dp))

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
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun NavigationSideBarPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            NavigationSideBar(
                currentDestination = Screen.Settings,
                showStatisticsButton = true,
            )
        }
    }
}
