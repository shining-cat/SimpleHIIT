package fr.shiningcat.simplehiit.android.tv.ui.home.contents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonFilled
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreens
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.home.R
import kotlinx.coroutines.delay
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun HomeMissingUsersContent(navigateToSettings: () -> Unit = {}) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(1000L) // wait a sec to increase awareness of the user of the focusing on the main button
        focusRequester.requestFocus()
    }

    Column(
        modifier =
            Modifier
                .padding(dimensionResource(CommonResourcesR.dimen.spacing_1))
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(id = CommonResourcesR.string.no_user_exist_title),
            style = MaterialTheme.typography.headlineLarge,
        )
        Image(
            modifier =
                Modifier
                    .size(dimensionResource(R.dimen.error_icon_size))
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
            text = stringResource(id = CommonResourcesR.string.warning_no_user_exist),
            style = MaterialTheme.typography.headlineMedium,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 0.dp,
                        vertical = dimensionResource(CommonResourcesR.dimen.spacing_3),
                    ),
        )
        Row {
            ButtonFilled(
                modifier =
                    Modifier
                        .fillMaxWidth(.3f)
                        .focusRequester(focusRequester),
                fillHeight = true,
                fillWidth = true,
                // calling focus on the first setting on opening
                label = stringResource(id = CommonResourcesR.string.go_to_settings),
                icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                iconContentDescription = CommonResourcesR.string.settings_button_content_label,
                accentColor = true,
                onClick = navigateToSettings,
            )
        }
    }
}

// Previews
@ExperimentalTvMaterial3Api
@PreviewTvScreens
@Composable
private fun HomeMissingUsersContentPreviewPhonePortrait() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            HomeMissingUsersContent()
        }
    }
}
