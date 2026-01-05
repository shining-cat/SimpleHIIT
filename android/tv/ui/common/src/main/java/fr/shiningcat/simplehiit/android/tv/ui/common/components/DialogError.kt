package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.R
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreensNoUi
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun DialogError(
    errorMessage: String,
    errorCode: String,
    dismissButtonLabel: String = "",
    dismissAction: () -> Unit,
) {
    DialogContentLayout(
        onDismissRequest = dismissAction,
        title = stringResource(id = CommonResourcesR.string.error_title),
        image = CommonResourcesR.drawable.warning,
        imageContentDescription = CommonResourcesR.string.warning_icon_content_description,
        content = {
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
        },
        buttons = {
            ButtonText(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height))),
                fillWidth = true,
                fillHeight = true,
                label = dismissButtonLabel,
                onClick = dismissAction,
            )
        },
    )
}

// Previews
@PreviewTvScreensNoUi
@Composable
private fun DialogErrorPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            DialogError(
                errorMessage = "A balloon is floating above the country",
                errorCode = "1234",
                dismissAction = {},
                dismissButtonLabel = "OK",
            )
        }
    }
}
