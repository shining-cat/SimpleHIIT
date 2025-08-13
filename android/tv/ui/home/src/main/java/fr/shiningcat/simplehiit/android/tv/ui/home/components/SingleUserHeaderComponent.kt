package fr.shiningcat.simplehiit.android.tv.ui.home.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.models.User

@Composable
fun SingleUserHeaderComponent(
    modifier: Modifier = Modifier,
    user: User,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.single_user_header_title),
            style = MaterialTheme.typography.headlineLarge,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = user.name,
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

// Previews
@ExperimentalTvMaterial3Api
@Preview(
    name = "light mode, fontscale 1",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 1f,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "dark mode, fontscale 1",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 1f,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "light mode, fontscale 1.5",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 1.5f,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "dark mode, fontscale 1.5",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 1.5f,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "light mode, fontscale 2",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 2f,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "dark mode, fontscale 2",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 2f,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun SingleUserHeaderComponentComponentPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            SingleUserHeaderComponent(
                user = User(123L, "User 1", selected = true),
                modifier = Modifier.height(250.dp),
            )
        }
    }
}
