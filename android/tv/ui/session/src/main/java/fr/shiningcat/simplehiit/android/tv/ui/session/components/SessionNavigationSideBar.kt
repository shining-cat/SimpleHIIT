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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.components.SideBarItem
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreens
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun SessionNavigationSideBar(
    @StringRes title: Int,
    onBackButtonClick: () -> Unit = {},
    @StringRes backButtonLabel: Int,
) {
    val adaptedWidth = adaptDpToFontScale(160.dp)
    Column(
        modifier =
            Modifier
                .fillMaxHeight()
                .width(adaptedWidth)
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
    ) {
        Text(
            text = stringResource(id = title),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(32.dp))

        SideBarItem(
            onClick = onBackButtonClick,
            icon = R.drawable.arrow_back,
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
                title = R.string.session_work_page_title,
                backButtonLabel = R.string.pause,
            )
        }
    }
}
