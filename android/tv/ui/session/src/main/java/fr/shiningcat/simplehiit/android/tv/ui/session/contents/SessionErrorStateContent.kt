package fr.shiningcat.simplehiit.android.tv.ui.session.contents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonError
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreens
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.session.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.sharedui.session.SessionViewState
import fr.shiningcat.simplehiit.commonresources.R as CommonResources

@Composable
fun SessionErrorStateContent(
    screenViewState: SessionViewState.Error,
    navigateUp: () -> Boolean,
    onAbort: () -> Unit,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    Column(
        modifier =
            Modifier
                .padding(dimensionResource(CommonResources.dimen.spacing_1))
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier =
                Modifier
                    .size(adaptDpToFontScale(dimensionResource(R.dimen.error_icon_size)))
                    .padding(
                        horizontal = 0.dp,
                        vertical = dimensionResource(CommonResources.dimen.spacing_2),
                    ),
            painter = painterResource(id = CommonResources.drawable.warning),
            contentDescription = stringResource(id = CommonResources.string.warning_icon_content_description),
        )
        Text(
            textAlign = TextAlign.Center,
            modifier =
                Modifier.padding(
                    horizontal = 0.dp,
                    vertical = dimensionResource(CommonResources.dimen.spacing_2),
                ),
            text =
                if (screenViewState.errorCode == Constants.Errors.SESSION_NOT_FOUND.code) {
                    stringResource(id = CommonResources.string.error_session_abort)
                } else {
                    stringResource(id = CommonResources.string.error_session_retry)
                },
            style = MaterialTheme.typography.headlineMedium,
        )
        if (screenViewState.errorCode.isNotBlank()) {
            Text(
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .padding(
                            horizontal = 0.dp,
                            vertical = dimensionResource(CommonResources.dimen.spacing_2),
                        ),
                text =
                    stringResource(
                        id = CommonResources.string.error_code,
                        screenViewState.errorCode,
                    ),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        val clickAction: () -> Unit = {
            if (screenViewState.errorCode == Constants.Errors.SESSION_NOT_FOUND.code) {
                onAbort()
            } else {
                navigateUp()
            }
        }
        ButtonError(
            modifier =
                Modifier
                    .padding(
                        horizontal = 0.dp,
                        vertical = dimensionResource(CommonResources.dimen.spacing_2),
                    ),
            onClick = clickAction,
            label =
                if (screenViewState.errorCode == Constants.Errors.SESSION_NOT_FOUND.code) {
                    stringResource(id = CommonResources.string.abort_session_button_label)
                } else {
                    stringResource(id = CommonResources.string.exit_button_label)
                },
        )
    }
}

// Previews
@ExperimentalTvMaterial3Api
@PreviewTvScreens
@Composable
private fun SessionErrorStateContentPreview(
    @PreviewParameter(SessionErrorStateContentPreviewParameterProvider::class) sessionViewState: SessionViewState.Error,
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            SessionErrorStateContent(
                screenViewState = sessionViewState,
                navigateUp = { true },
                onAbort = {},
            )
        }
    }
}

internal class SessionErrorStateContentPreviewParameterProvider : PreviewParameterProvider<SessionViewState.Error> {
    override val values: Sequence<SessionViewState.Error>
        get() =
            sequenceOf(
                SessionViewState.Error(errorCode = "ABCD-123"),
                SessionViewState.Error(errorCode = ""),
                SessionViewState.Error(errorCode = Constants.Errors.SESSION_NOT_FOUND.code),
            )
}
