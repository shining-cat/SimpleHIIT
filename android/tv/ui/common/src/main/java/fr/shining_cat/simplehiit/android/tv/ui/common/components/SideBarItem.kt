package fr.shining_cat.simplehiit.android.tv.ui.common.components

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.commonresources.R

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SideBarItem(
    onClick: () -> Unit = {},
    @DrawableRes
    icon: Int,
    @StringRes
    label: Int,
    selected: Boolean = false
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        colors = ButtonDefaults.colors(
            containerColor = MaterialTheme.colorScheme.primary, //same as NavigationDrawer background -> we should not see the container in the default state
            contentColor = if(selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onPrimary,
            focusedContainerColor = if(selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            focusedContentColor = if(selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface,
            pressedContainerColor = if(selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            pressedContentColor = if(selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
        ),
        shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = stringResource(id = label),
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text = stringResource(label))
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
private fun SideBarItemPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SideBarItem(
                    icon = R.drawable.home,
                    label = R.string.home_page_title,
                    selected = true
                )
                SideBarItem(
                    icon = R.drawable.cog,
                    label = R.string.settings_button_content_label,
                    selected = false
                )
                SideBarItem(
                    icon = R.drawable.bar_chart,
                    label = R.string.statistics_button_content_label,
                    selected = false
                )
                SideBarItem(
                    icon = R.drawable.home,
                    label = R.string.home_page_title,
                    selected = false
                )
                SideBarItem(
                    icon = R.drawable.cog,
                    label = R.string.settings_button_content_label,
                    selected = true
                )
                SideBarItem(
                    icon = R.drawable.bar_chart,
                    label = R.string.statistics_button_content_label,
                    selected = false
                )
            }
        }
    }
}