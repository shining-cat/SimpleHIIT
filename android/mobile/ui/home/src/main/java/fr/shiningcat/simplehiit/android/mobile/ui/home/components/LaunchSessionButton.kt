package fr.shiningcat.simplehiit.android.mobile.ui.home.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun LaunchSessionButton(
    modifier: Modifier = Modifier,
    canLaunchSession: Boolean,
    navigateToSession: () -> Unit = {},
) {
    Button(
        enabled = canLaunchSession,
        modifier =
            modifier
                .padding(vertical = 24.dp)
                .height(56.dp),
        onClick = navigateToSession,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
            ),
    ) {
        if (canLaunchSession) {
            Text(text = stringResource(id = R.string.launch_session_label))
        } else {
            Text(text = stringResource(id = R.string.cannot_launch_session_label))
        }
    }
}

// Previews
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun LaunchSessionButtonPreview() {
    SimpleHiitMobileTheme {
        Surface {
            Column {
                LaunchSessionButton(canLaunchSession = true)
                LaunchSessionButton(canLaunchSession = false)
            }
        }
    }
}
