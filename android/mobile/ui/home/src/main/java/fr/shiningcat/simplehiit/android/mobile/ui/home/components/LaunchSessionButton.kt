package fr.shiningcat.simplehiit.android.mobile.ui.home.components

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.home.R
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

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
                .padding(vertical = dimensionResource(CommonResourcesR.dimen.spacing_3))
                .height(dimensionResource(R.dimen.large_button_height)),
        onClick = navigateToSession,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
            ),
    ) {
        if (canLaunchSession) {
            Text(text = stringResource(id = CommonResourcesR.string.launch_session_label))
        } else {
            Text(text = stringResource(id = CommonResourcesR.string.cannot_launch_session_label))
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
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
