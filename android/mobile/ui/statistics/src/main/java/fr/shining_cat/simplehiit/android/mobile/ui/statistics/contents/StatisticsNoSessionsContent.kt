package fr.shining_cat.simplehiit.android.mobile.ui.statistics.contents

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.android.mobile.ui.common.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.android.mobile.ui.statistics.components.StatisticsHeaderComponent
import fr.shining_cat.simplehiit.commonresources.R

@Composable
fun StatisticsNoSessionsContent(
    userName: String,
    showUsersSwitch: Boolean,
    openUserPicker: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        StatisticsHeaderComponent(
            openUserPicker = openUserPicker,
            currentUserName = userName,
            showUsersSwitch = showUsersSwitch
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.doge),
            contentDescription = stringResource(id = R.string.doge_icon_content_description),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 16.dp)
                .fillMaxWidth(),
            text = stringResource(id = R.string.no_users_found_error_message),
            style = MaterialTheme.typography.headlineMedium,
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
private fun StatisticsNoSessionsContentPreview() {
    SimpleHiitTheme {
        Surface {
            StatisticsNoSessionsContent(
                userName = "Georges",
                showUsersSwitch = true
            )
        }
    }
}

