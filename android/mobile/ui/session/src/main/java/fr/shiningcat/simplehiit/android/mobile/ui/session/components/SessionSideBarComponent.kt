package fr.shiningcat.simplehiit.android.mobile.ui.session.components

import android.content.res.Configuration
import androidx.annotation.StringRes
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
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.SideBarItem
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun SessionSideBarComponent(
    @StringRes title: Int,
    onBackButtonClick: () -> Unit = {},
    @StringRes backButtonLabel: Int
) {
    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        header = {
            Text(
                text = stringResource(title),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) {
        SideBarItem(
            onClick = onBackButtonClick,
            icon = R.drawable.arrow_back,
            label = backButtonLabel,
            selected = false
        )
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
private fun SessionSideBarComponentPreview() {
    SimpleHiitMobileTheme {
        Surface {
            SessionSideBarComponent(
                title = R.string.session_work_page_title,
                backButtonLabel = R.string.pause
            )
        }
    }
}
