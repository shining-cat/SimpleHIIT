package fr.shiningcat.simplehiit.android.mobile.ui.common.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
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
    NavigationRailItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = stringResource(id = label),
            )
        },
        label = { Text(text = stringResource(label)) },
        colors =
            NavigationRailItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.secondary,
                selectedTextColor = MaterialTheme.colorScheme.secondary,
                // primary will be the color of the NavigationSideBar, so this indicatorColor will be invisible, as wanted.
                // Using Color.Transparent punches a whole through the SideBar itself, which is not what we want
                indicatorColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                disabledIconColor = MaterialTheme.colorScheme.onPrimary,
                disabledTextColor = MaterialTheme.colorScheme.onPrimary,
            ),
    )
}

// Previews
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun SideBarItemPreview() {
    SimpleHiitMobileTheme {
        Surface {
            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
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
            }
        }
    }
}
