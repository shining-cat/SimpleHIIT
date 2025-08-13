package fr.shiningcat.simplehiit.android.mobile.ui.session.components

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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme

@Composable
fun RemainingPercentageComponent(
    modifier: Modifier,
    label: String,
    percentage: Float,
    thickness: Dp,
    bicolor: Boolean,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        LinearProgressIndicator(
            progress = { percentage },
            modifier =
                Modifier
                    .height(thickness)
                    .fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = if (bicolor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            strokeCap = StrokeCap.Butt,
            gapSize = 0.dp,
            drawStopIndicator = {},
        )
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@Composable
private fun RemainingPercentageComponentPreview() {
    SimpleHiitMobileTheme {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                RemainingPercentageComponent(
                    modifier =
                        Modifier
                            .padding(horizontal = 16.dp),
                    label = "Next exercise in 3s",
                    percentage = .3f,
                    thickness = 8.dp,
                    bicolor = false,
                )
                RemainingPercentageComponent(
                    modifier =
                        Modifier
                            .padding(horizontal = 16.dp),
                    label = "Next rest in 7mn\u00A023s",
                    percentage = .8f,
                    thickness = 8.dp,
                    bicolor = false,
                )
                RemainingPercentageComponent(
                    modifier =
                        Modifier
                            .padding(horizontal = 16.dp),
                    label = "Total remaining: 1h\u00A020mn\u00A037s",
                    percentage = .79f,
                    thickness = 16.dp,
                    bicolor = true,
                )
            }
        }
    }
}
