package fr.shiningcat.simplehiit.android.mobile.ui.home.contents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.home.R
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun HomeErrorContent(
    errorCode: String,
    resetWholeApp: () -> Unit = {},
) {
    Column(
        modifier =
            Modifier
                .padding(dimensionResource(CommonResourcesR.dimen.spacing_1))
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        Image(
            modifier =
                Modifier
                    .size(adaptDpToFontScale(dimensionResource(R.dimen.error_symbol_size)))
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
                Modifier.padding(
                    horizontal = 0.dp,
                    vertical = dimensionResource(CommonResourcesR.dimen.spacing_2),
                ),
            text = stringResource(id = CommonResourcesR.string.error_irrecoverable_state),
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
        Button(
            modifier =
                Modifier
                    .padding(
                        horizontal = 0.dp,
                        vertical = dimensionResource(CommonResourcesR.dimen.spacing_2),
                    ).align(Alignment.CenterHorizontally),
            onClick = resetWholeApp,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
        ) {
            Text(text = stringResource(id = CommonResourcesR.string.reset_app_button_label))
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun HomeErrorContentPreview() {
    SimpleHiitMobileTheme {
        Surface {
            Surface {
                HomeErrorContent(
                    errorCode = "An error happened",
                    resetWholeApp = {},
                )
            }
        }
    }
}
