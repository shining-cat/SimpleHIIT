package fr.shiningcat.simplehiit.android.tv.ui.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonFilled
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreensNoUi
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import kotlinx.coroutines.delay

@Composable
fun StatisticsHeaderComponent(
    openUserPicker: () -> Unit = {},
    currentUserName: String,
    showUsersSwitch: Boolean,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        if (showUsersSwitch) {
            delay(1000L) // wait a sec to increase awareness of the user of the focusing on the main button
            focusRequester.requestFocus()
        }
    }

    Row(
        modifier =
            Modifier
                .padding(dimensionResource(R.dimen.spacing_2))
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            text = currentUserName,
        )
        if (showUsersSwitch) {
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_4)))
            ButtonFilled(
                modifier = Modifier.focusRequester(focusRequester),
                onClick = { openUserPicker() },
                icon = ImageVector.vectorResource(R.drawable.switch_user),
                iconContentDescription = R.string.statistics_page_switch_user,
            )
        }
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@PreviewTvScreensNoUi
@Composable
private fun StatisticsHeaderPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            StatisticsHeaderComponent(
                currentUserName = "Charles-Antoine",
                showUsersSwitch = true,
            )
        }
    }
}
