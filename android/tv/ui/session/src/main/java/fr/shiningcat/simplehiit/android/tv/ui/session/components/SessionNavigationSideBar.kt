package fr.shiningcat.simplehiit.android.tv.ui.session.components

import androidx.annotation.StringRes
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
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.components.SideBarItem
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreens
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.common.R as CommonResourcesTvR
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun SessionNavigationSideBar(
    @StringRes title: Int,
    onBackButtonClick: () -> Unit = {},
    @StringRes backButtonLabel: Int,
) {
    val adaptedWidth = adaptDpToFontScale(dimensionResource(CommonResourcesTvR.dimen.navigation_side_bar_width))
    Column(
        modifier =
            Modifier
                .fillMaxHeight()
                .width(adaptedWidth)
                .background(MaterialTheme.colorScheme.primary)
                .padding(dimensionResource(CommonResourcesR.dimen.spacing_2)),
    ) {
        Text(
            text = stringResource(id = title),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(dimensionResource(CommonResourcesR.dimen.spacing_4)))

        SideBarItem(
            onClick = onBackButtonClick,
            icon = CommonResourcesR.drawable.arrow_back,
            label = backButtonLabel,
            selected = false,
        )
    }
}

// Previews
@PreviewTvScreens
@Composable
private fun NavigationSideBarPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            SessionNavigationSideBar(
                title = CommonResourcesR.string.session_work_page_title,
                backButtonLabel = CommonResourcesR.string.pause,
            )
        }
    }
}
