package fr.shiningcat.simplehiit.android.mobile.ui.session.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme

@Composable
fun RemainingPercentageComponent(
    modifier: Modifier,
    label: String,
    percentage: Float,
    thickness: Dp,
    bicolor: Boolean
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge
        )
        LinearProgressIndicator(
            modifier = Modifier
                .height(thickness)
                .fillMaxWidth(),
            progress = percentage,
            color = MaterialTheme.colorScheme.secondary,
            trackColor = if (bicolor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        )
    }
}

// Previews
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RemainingPercentageComponentPreview() {
    SimpleHiitMobileTheme {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                RemainingPercentageComponent(
                    modifier = Modifier
                        .padding(horizontal = 64.dp)
                        .height(100.dp),
                    label = "Next exercise in 3s",
                    percentage = .3f,
                    thickness = 8.dp,
                    bicolor = false
                )
                RemainingPercentageComponent(
                    modifier = Modifier
                        .padding(horizontal = 64.dp)
                        .height(100.dp),
                    label = "Next rest in 23s",
                    percentage = .8f,
                    thickness = 8.dp,
                    bicolor = false
                )
                RemainingPercentageComponent(
                    modifier = Modifier
                        .padding(horizontal = 64.dp)
                        .height(100.dp),
                    label = "Total remaining: 20mn 37s",
                    percentage = .79f,
                    thickness = 16.dp,
                    bicolor = true
                )
            }
        }
    }
}
