package fr.shining_cat.simplehiit.android.mobile.ui.statistics.contents

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.android.mobile.ui.common.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.android.mobile.ui.statistics.components.StatisticsHeader
import fr.shining_cat.simplehiit.commonresources.R

@Composable
fun StatisticsErrorContent(
    userName: String,
    errorCode: String,
    deleteSessionsForUser: () -> Unit = {},
    showUsersSwitch: Boolean = false,
    openUserPicker: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        StatisticsHeader(
            openUserPicker = openUserPicker,
            currentUserName = userName,
            showUsersSwitch = showUsersSwitch
        )

        Image(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 0.dp, vertical = 16.dp),
            painter = painterResource(id = R.drawable.warning),
            contentDescription = stringResource(id = R.string.warning_icon_content_description)
        )
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 16.dp),
            text = stringResource(id = R.string.error_irrecoverable_statistics, userName),
            style = MaterialTheme.typography.headlineMedium,
        )
        if (errorCode.isNotBlank()) {
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 0.dp, vertical = 16.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.error_code, errorCode),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        Button(
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 16.dp)
                .align(Alignment.CenterHorizontally),
            onClick = deleteSessionsForUser,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text(text = stringResource(id = R.string.delete_button_label))
        }
    }
}

// Previews
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun StatisticsErrorContentPreview() {
    SimpleHiitTheme {
        Surface {
            StatisticsErrorContent(
                userName = "Charles-Antoine",
                errorCode = "ABCD-123"
            )
        }
    }
}
