package fr.shiningcat.simplehiit.android.tv.ui.statistics.contents

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.statistics.components.StatisticsHeaderComponent
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun StatisticsNoSessionsContent(
    userName: String,
    showUsersSwitch: Boolean,
    openUserPicker: () -> Unit = {},
) {
    Column(
        modifier =
            Modifier
                .padding(8.dp)
                .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        StatisticsHeaderComponent(
            openUserPicker = openUserPicker,
            currentUserName = userName,
            showUsersSwitch = showUsersSwitch,
        )

        Icon(
            modifier =
                Modifier
                    .weight(.5f, true)
                    .align(CenterHorizontally),
            imageVector = ImageVector.vectorResource(R.drawable.doge),
            contentDescription = stringResource(id = R.string.doge_icon_content_description),
            tint = MaterialTheme.colorScheme.onPrimary,
        )
        Text(
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .padding(horizontal = 0.dp, vertical = 16.dp)
                    .fillMaxWidth(),
            text = stringResource(id = R.string.no_users_found_error_message),
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

// Previews
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun SettingsErrorContentPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            StatisticsNoSessionsContent(
                userName = "Georges",
                showUsersSwitch = true,
            )
        }
    }
}
