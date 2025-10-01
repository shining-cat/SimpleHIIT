package fr.shiningcat.simplehiit.android.mobile.ui.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.mobile.ui.common.R
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R as commonResourcesR

@Composable
fun ErrorDialog(
    errorMessage: String,
    errorCode: String,
    dismissButtonLabel: String = "",
    dismissAction: () -> Unit,
) {
    Dialog(onDismissRequest = dismissAction) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(dimensionResource(commonResourcesR.dimen.spacing_1))
                        .fillMaxWidth(),
            ) {
                Text(
                    textAlign = TextAlign.Left,
                    modifier =
                        Modifier.padding(
                            horizontal = 0.dp,
                            vertical = dimensionResource(commonResourcesR.dimen.spacing_05),
                        ),
                    text = stringResource(id = commonResourcesR.string.error_title),
                    style = MaterialTheme.typography.headlineSmall,
                )
                Image(
                    modifier =
                        Modifier
                            .size(adaptDpToFontScale(dimensionResource(R.dimen.dialog_main_icon_size)))
                            .align(Alignment.CenterHorizontally)
                            .padding(
                                horizontal = 0.dp,
                                vertical = dimensionResource(commonResourcesR.dimen.spacing_3),
                            ),
                    painter = painterResource(id = commonResourcesR.drawable.warning),
                    contentDescription = stringResource(id = commonResourcesR.string.warning_icon_content_description),
                )
                Text(
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 0.dp,
                                vertical = dimensionResource(commonResourcesR.dimen.spacing_1),
                            ),
                    text = stringResource(id = commonResourcesR.string.error_notice),
                    style = MaterialTheme.typography.bodyMedium,
                )
                if (errorMessage.isNotBlank()) {
                    Text(
                        textAlign = TextAlign.Center,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 0.dp,
                                    vertical = dimensionResource(commonResourcesR.dimen.spacing_1),
                                ),
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Text(
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 0.dp,
                                vertical = dimensionResource(commonResourcesR.dimen.spacing_1),
                            ),
                    text = stringResource(id = commonResourcesR.string.error_code, errorCode),
                    style = MaterialTheme.typography.bodyMedium,
                )
                TextButton(onClick = dismissAction) {
                    Text(text = dismissButtonLabel)
                }
            }
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun ErrorDialogPreview() {
    SimpleHiitMobileTheme {
        Surface {
            ErrorDialog(
                errorMessage = "A balloon is floating above the country",
                errorCode = "1234",
                dismissAction = {},
                dismissButtonLabel = "OK",
            )
        }
    }
}
