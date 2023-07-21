@file:OptIn(ExperimentalTvMaterial3Api::class)

package fr.shining_cat.simplehiit.android.tv.ui.home.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme

import fr.shining_cat.simplehiit.commonresources.R

@Composable
fun GoToSettingsButton(
    modifier: Modifier = Modifier,
    navigateToSettings: () -> Unit = {}
) {
    Button(
        modifier = modifier
            .padding(vertical = 24.dp)
            .height(56.dp),
        onClick = navigateToSettings,
        colors = ButtonDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Text(text = stringResource(id = R.string.go_to_settings))
    }
}

// Previews
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun GoToSettingsButtonPreview() {
    SimpleHiitTvTheme {
        Surface {
            GoToSettingsButton()
        }
    }
}
