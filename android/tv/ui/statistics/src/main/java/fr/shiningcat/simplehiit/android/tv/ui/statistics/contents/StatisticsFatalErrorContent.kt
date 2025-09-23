package fr.shiningcat.simplehiit.android.tv.ui.statistics.contents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import fr.shiningcat.simplehiit.commonresources.R
import kotlinx.coroutines.delay

@Composable
fun StatisticsFatalErrorContent(
    errorCode: String,
    resetWholeApp: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(1000L) // wait a sec to increase awareness of the user of the focusing on the main button
        focusRequester.requestFocus()
    }

    Column(
        modifier =
            Modifier
                .padding(dimensionResource(R.dimen.spacing_1))
                .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier =
                Modifier
                    .size(adaptDpToFontScale(120.dp))
                    .align(Alignment.CenterHorizontally)
                    .padding(
                        horizontal = 0.dp,
                        vertical = dimensionResource(R.dimen.spacing_2)
                    ),
            painter = painterResource(id = R.drawable.warning),
            contentDescription = stringResource(id = R.string.warning_icon_content_description),
        )
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                horizontal = 0.dp,
                vertical = dimensionResource(R.dimen.spacing_2)
            ),
            text = stringResource(id = R.string.error_irrecoverable_state),
            style = MaterialTheme.typography.headlineMedium,
        )
        if (errorCode.isNotBlank()) {
            Text(
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .padding(
                            horizontal = 0.dp,
                            vertical = dimensionResource(R.dimen.spacing_2)
                        )
                        .align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.error_code, errorCode),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        ButtonError(
            modifier =
                Modifier
                    .padding(
                        horizontal = 0.dp,
                        vertical = dimensionResource(R.dimen.spacing_2)
                    )
                    .align(Alignment.CenterHorizontally)
                    .focusRequester(focusRequester),
            // calling focus on button on opening
            onClick = resetWholeApp,
            label = stringResource(id = R.string.reset_app_button_label),
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
            StatisticsFatalErrorContent(errorCode = "ABCD-123")
        }
    }
}
