package fr.shiningcat.simplehiit.android.mobile.ui.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import fr.shiningcat.simplehiit.android.mobile.ui.common.R
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptiveDialogProperties
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptiveDialogWidth
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun ErrorDialog(
    errorMessage: String,
    errorCode: String,
    dismissButtonLabel: String = "",
    dismissAction: () -> Unit,
) {
    Dialog(
        onDismissRequest = dismissAction,
        properties = adaptiveDialogProperties(),
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.adaptiveDialogWidth(),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(dimensionResource(CommonResourcesR.dimen.spacing_1))
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
            ) {
                Text(
                    textAlign = TextAlign.Left,
                    modifier =
                        Modifier.padding(
                            horizontal = 0.dp,
                            vertical = dimensionResource(CommonResourcesR.dimen.spacing_05),
                        ),
                    text = stringResource(id = CommonResourcesR.string.error_title),
                    style = MaterialTheme.typography.headlineSmall,
                )
                Image(
                    modifier =
                        Modifier
                            .size(adaptDpToFontScale(dimensionResource(R.dimen.dialog_main_icon_size)))
                            .align(Alignment.CenterHorizontally)
                            .padding(
                                horizontal = 0.dp,
                                vertical = dimensionResource(CommonResourcesR.dimen.spacing_3),
                            ),
                    painter = painterResource(id = CommonResourcesR.drawable.warning),
                    contentDescription = stringResource(id = CommonResourcesR.string.warning_icon_content_description),
                )
                Text(
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 0.dp,
                                vertical = dimensionResource(CommonResourcesR.dimen.spacing_1),
                            ),
                    text = stringResource(id = CommonResourcesR.string.error_notice),
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
                                    vertical = dimensionResource(CommonResourcesR.dimen.spacing_1),
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
                                vertical = dimensionResource(CommonResourcesR.dimen.spacing_1),
                            ),
                    text = stringResource(id = CommonResourcesR.string.error_code, errorCode),
                    style = MaterialTheme.typography.bodyMedium,
                )
                OnSurfaceTextButton(onClick = dismissAction) {
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
