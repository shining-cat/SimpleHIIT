package fr.shiningcat.simplehiit.android.mobile.ui.statistics.contents

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
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.R
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.StatisticsViewState
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.components.StatisticsHeaderComponent
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun StatisticsErrorContent(
    modifier: Modifier = Modifier,
    errorViewState: StatisticsViewState.Error,
    deleteSessionsForUser: () -> Unit = {},
    uiArrangement: UiArrangement,
    onUserSelected: (User) -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(
                    top = if (uiArrangement == UiArrangement.VERTICAL) dimensionResource(CommonResourcesR.dimen.spacing_1) else 0.dp,
                    start = dimensionResource(CommonResourcesR.dimen.spacing_1),
                    end = dimensionResource(CommonResourcesR.dimen.spacing_1),
                ).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StatisticsHeaderComponent(
            currentUserName = errorViewState.selectedUser.name,
            allUsers = errorViewState.allUsers,
            uiArrangement = uiArrangement,
            onUserSelected = onUserSelected,
        )

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
            text =
                stringResource(
                    id = CommonResourcesR.string.error_irrecoverable_statistics,
                    errorViewState.selectedUser.name,
                ),
            style = MaterialTheme.typography.headlineMedium,
        )
        if (errorViewState.errorCode.isNotBlank()) {
            Text(
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .padding(
                            horizontal = 0.dp,
                            vertical = dimensionResource(CommonResourcesR.dimen.spacing_2),
                        ).align(Alignment.CenterHorizontally),
                text =
                    stringResource(
                        id = CommonResourcesR.string.error_code,
                        errorViewState.errorCode,
                    ),
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
            onClick = deleteSessionsForUser,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
        ) {
            Text(text = stringResource(id = CommonResourcesR.string.delete_button_label))
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun StatisticsErrorContentPreview(
    @PreviewParameter(StatisticsErrorContentPreviewParameterProvider::class) viewState: StatisticsViewState.Error,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val previewUiArrangement: UiArrangement =
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) { // typically, a tablet or bigger in landscape
            UiArrangement.HORIZONTAL
        } else { // WindowWidthSizeClass.Medium, WindowWidthSizeClass.Compact :
            if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) { // typically, a phone in landscape
                UiArrangement.HORIZONTAL
            } else {
                UiArrangement.VERTICAL // typically, a phone or tablet in portrait
            }
        }
    SimpleHiitMobileTheme {
        Surface {
            StatisticsErrorContent(
                uiArrangement = previewUiArrangement,
                errorViewState = viewState,
            )
        }
    }
}

internal class StatisticsErrorContentPreviewParameterProvider : PreviewParameterProvider<StatisticsViewState.Error> {
    override val values: Sequence<StatisticsViewState.Error>
        get() =
            sequenceOf(
                StatisticsViewState.Error(
                    allUsers =
                        listOf(
                            User(name = "Alice"),
                            User(name = "Bob"),
                            User(name = "Charlie"),
                        ),
                    selectedUser = User(name = "Sven Svensson"),
                    errorCode = "error code preview 1",
                ),
                StatisticsViewState.Error(
                    allUsers =
                        listOf(
                            User(name = "Sven"),
                        ),
                    selectedUser = User(name = "Sven Svensson"),
                    errorCode = "error code preview 1",
                ),
            )
}
