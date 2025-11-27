package fr.shiningcat.simplehiit.android.tv.ui.statistics.contents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonError
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreensNoUi
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.statistics.R
import fr.shiningcat.simplehiit.android.tv.ui.statistics.components.StatisticsHeaderComponent
import kotlinx.coroutines.delay
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun StatisticsErrorContent(
    userName: String,
    errorCode: String,
    deleteSessionsForUser: () -> Unit = {},
    showUsersSwitch: Boolean = false,
    openUserPicker: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(1000L) // wait a sec to increase awareness of the user of the focusing on the main button
        focusRequester.requestFocus()
    }

    Column(
        modifier =
            Modifier
                .padding(dimensionResource(CommonResourcesR.dimen.spacing_1))
                .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StatisticsHeaderComponent(
            openUserPicker = openUserPicker,
            currentUserName = userName,
            showUsersSwitch = showUsersSwitch,
        )

        Image(
            modifier =
                Modifier
                    .size(adaptDpToFontScale(dimensionResource(R.dimen.error_icon_size)))
                    .align(Alignment.CenterHorizontally)
                    .padding(
                        horizontal = 0.dp,
                        vertical = dimensionResource(CommonResourcesR.dimen.spacing_2),
                    ),
            painter = painterResource(id = CommonResourcesR.drawable.warning),
            contentDescription = stringResource(id = CommonResourcesR.string.warning_icon_content_description),
        )
        Text(
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .padding(
                        horizontal = 0.dp,
                        vertical = dimensionResource(CommonResourcesR.dimen.spacing_2),
                    ).fillMaxWidth(.5f),
            text =
                stringResource(
                    id = CommonResourcesR.string.error_irrecoverable_statistics,
                    userName,
                ),
            style = MaterialTheme.typography.headlineMedium,
        )
        if (errorCode.isNotBlank()) {
            Text(
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .padding(
                            horizontal = 0.dp,
                            vertical = dimensionResource(CommonResourcesR.dimen.spacing_2),
                        ).align(Alignment.CenterHorizontally),
                text = stringResource(id = CommonResourcesR.string.error_code, errorCode),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        ButtonError(
            modifier =
                Modifier
                    .padding(
                        horizontal = 0.dp,
                        vertical = dimensionResource(CommonResourcesR.dimen.spacing_2),
                    ).focusRequester(focusRequester),
            // calling focus on button on opening
            onClick = deleteSessionsForUser,
            label = stringResource(id = CommonResourcesR.string.delete_button_label),
        )
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@PreviewTvScreensNoUi
@Composable
private fun SettingsErrorContentPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            StatisticsErrorContent(
                userName = "Charles-Antoine",
                errorCode = "ABCD-123",
                showUsersSwitch = true,
            )
        }
    }
}
