package fr.shiningcat.simplehiit.android.tv.ui.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.tv.ui.common.components.CustomLinearProgressIndicator
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme

@Composable
fun RemainingPercentageComponent(
    modifier: Modifier,
    label: String,
    percentage: Float,
    thickness: Dp,
    bicolor: Boolean,
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        CustomLinearProgressIndicator(
            modifier =
                Modifier
                    .height(thickness)
                    .fillMaxWidth(),
            currentValue = (percentage * 100).toInt(),
            progressColor = MaterialTheme.colorScheme.secondary,
            trackColor = if (bicolor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            borderColor = MaterialTheme.colorScheme.secondary,
        )
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun RemainingPercentageComponentPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                RemainingPercentageComponent(
                    modifier =
                        Modifier
                            .padding(horizontal = 64.dp)
                            .height(100.dp),
                    label = "Next exercise in 3s",
                    percentage = .3f,
                    thickness = 8.dp,
                    bicolor = false,
                )
                RemainingPercentageComponent(
                    modifier =
                        Modifier
                            .padding(horizontal = 64.dp)
                            .height(100.dp),
                    label = "Next rest in 23s",
                    percentage = .8f,
                    thickness = 8.dp,
                    bicolor = false,
                )
                RemainingPercentageComponent(
                    modifier =
                        Modifier
                            .padding(horizontal = 64.dp)
                            .height(100.dp),
                    label = "Total remaining: 20mn 37s",
                    percentage = .79f,
                    thickness = 16.dp,
                    bicolor = true,
                )
            }
        }
    }
}
