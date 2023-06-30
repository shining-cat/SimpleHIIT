package fr.shining_cat.simplehiit.android.mobile.ui.statistics.contents

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.android.mobile.ui.common.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.commonresources.R

@Composable
fun StatisticsNoUsersContent(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(paddingValues = paddingValues)
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.doge),
            contentDescription = stringResource(id = R.string.doge_icon_content_description),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 16.dp)
                .fillMaxWidth(),
            text = stringResource(id = R.string.no_users_found_error_message),
            style = MaterialTheme.typography.headlineMedium,
        )
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
private fun StatisticsNoUsersContentPreview() {
    SimpleHiitTheme {
        Surface {
            StatisticsNoUsersContent(paddingValues = PaddingValues(0.dp))
        }
    }
}
