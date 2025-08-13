package fr.shiningcat.simplehiit.android.mobile.ui.home.contents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.home.components.GoToSettingsButton
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun HomeMissingUsersContent(navigateToSettings: () -> Unit = {}) {
    Column(
        modifier =
            Modifier
                .padding(8.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.no_user_exist_title),
            style = MaterialTheme.typography.headlineLarge,
        )
        Image(
            modifier =
                Modifier
                    .size(adaptDpToFontScale(120.dp))
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 0.dp, vertical = 24.dp),
            painter = painterResource(id = R.drawable.warning),
            contentDescription = stringResource(id = R.string.warning_icon_content_description),
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.warning_no_user_exist),
            style = MaterialTheme.typography.headlineMedium,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp, vertical = 24.dp),
        )
        GoToSettingsButton(navigateToSettings = navigateToSettings)
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun HomeMissingUsersContentPreviewPhoneLandscape() {
    SimpleHiitMobileTheme {
        Surface {
            HomeMissingUsersContent()
        }
    }
}
