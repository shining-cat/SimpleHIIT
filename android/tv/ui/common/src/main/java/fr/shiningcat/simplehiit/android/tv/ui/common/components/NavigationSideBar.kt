package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.Screen
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.R
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreensNoUi
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

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
                .width(adaptDpToFontScale(dimensionResource(R.dimen.navigation_side_bar_width)))
                .background(MaterialTheme.colorScheme.primary)
                .padding(dimensionResource(CommonResourcesR.dimen.spacing_2)),
    ) {
        Text(
            text = stringResource(CommonResourcesR.string.app_name),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(dimensionResource(CommonResourcesR.dimen.spacing_4)))

        SideBarItem(
            onClick = { navigateTo(Screen.Home.route) },
            icon = CommonResourcesR.drawable.home,
            label = CommonResourcesR.string.home_page_title,
            selected = currentDestination == Screen.Home,
        )
        Spacer(modifier = Modifier.height(dimensionResource(CommonResourcesR.dimen.spacing_2)))

        SideBarItem(
            onClick = { navigateTo(Screen.Settings.route) },
            icon = CommonResourcesR.drawable.cog,
            label = CommonResourcesR.string.settings_button_content_label,
            selected = currentDestination == Screen.Settings,
        )

        Spacer(modifier = Modifier.height(dimensionResource(CommonResourcesR.dimen.spacing_2)))

        if (showStatisticsButton) {
            SideBarItem(
                onClick = { navigateTo(Screen.Statistics.route) },
                icon = CommonResourcesR.drawable.bar_chart,
                label = CommonResourcesR.string.statistics_button_content_label,
                selected = currentDestination == Screen.Statistics,
            )
        }
    }
}

// Previews
@PreviewTvScreensNoUi
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
