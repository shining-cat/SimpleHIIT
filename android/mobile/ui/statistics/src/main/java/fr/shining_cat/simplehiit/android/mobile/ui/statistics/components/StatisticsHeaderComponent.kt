package fr.shining_cat.simplehiit.android.mobile.ui.statistics.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.commonutils.HiitLogger

@Composable
fun StatisticsHeaderComponent(
    openUserPicker: () -> Unit = {},
    currentUserName: String,
    showUsersSwitch: Boolean,
    hiitLogger: HiitLogger? = null
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            text = currentUserName
        )
        if (showUsersSwitch) {
            Spacer(modifier = Modifier.width(32.dp))
            IconButton(onClick = { openUserPicker() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.switch_user),
                    contentDescription = stringResource(id = R.string.statistics_page_switch_user),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
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
private fun StatisticsHeaderPreview() {
    SimpleHiitMobileTheme {
        Surface {
            StatisticsHeaderComponent(
                currentUserName = "Charles-Antoine",
                showUsersSwitch = true
            )
        }
    }
}
