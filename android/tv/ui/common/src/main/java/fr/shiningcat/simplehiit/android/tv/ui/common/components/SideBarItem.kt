package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButtonDefaults.MediumIconSize
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun SideBarItem(
    onClick: () -> Unit = {},
    @DrawableRes
    icon: Int,
    @StringRes
    label: Int,
    selected: Boolean = false,
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        colors =
            ButtonDefaults.colors(
                // same as NavigationDrawer background -> we should not see the container in the default state
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onPrimary,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContentColor = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface,
                pressedContainerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                pressedContentColor = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface,
            ),
        shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = stringResource(id = label),
            modifier = Modifier.size(adaptDpToFontScale(MediumIconSize)),
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text = stringResource(label))
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun SideBarItemPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SideBarItem(
                    icon = R.drawable.home,
                    label = R.string.home_page_title,
                    selected = true,
                )
                SideBarItem(
                    icon = R.drawable.cog,
                    label = R.string.settings_button_content_label,
                    selected = false,
                )
                SideBarItem(
                    icon = R.drawable.bar_chart,
                    label = R.string.statistics_button_content_label,
                    selected = false,
                )
                SideBarItem(
                    icon = R.drawable.home,
                    label = R.string.home_page_title,
                    selected = false,
                )
                SideBarItem(
                    icon = R.drawable.cog,
                    label = R.string.settings_button_content_label,
                    selected = true,
                )
                SideBarItem(
                    icon = R.drawable.bar_chart,
                    label = R.string.statistics_button_content_label,
                    selected = false,
                )
            }
        }
    }
}
