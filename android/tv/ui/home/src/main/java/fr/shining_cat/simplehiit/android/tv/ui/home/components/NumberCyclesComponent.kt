package fr.shining_cat.simplehiit.android.tv.ui.home.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shining_cat.simplehiit.android.tv.ui.common.components.transparentButtonTextColors
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.commonresources.R

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun NumberCyclesComponent(
    modifier: Modifier = Modifier,
    decreaseNumberOfCycles: () -> Unit = {},
    increaseNumberOfCycles: () -> Unit = {},
    numberOfCycles: Int,
    lengthOfCycle: String,
    totalLengthFormatted: String
) {
    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            text = stringResource(id = R.string.number_of_cycle_setting_title)
        )
        Row(
            Modifier
                .padding(horizontal = 0.dp, vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                enabled = numberOfCycles > 1,
                modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp),
                onClick = decreaseNumberOfCycles,
                colors = transparentButtonTextColors(),
                shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small)
            ) {
                Text(
                    text = stringResource(id = R.string.minus),
                    fontWeight = FontWeight.Bold,
                    fontSize = 44.sp,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(
                    id = R.string.number_of_cycle_setting,
                    numberOfCycles,
                    lengthOfCycle
                ),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp),
                onClick = increaseNumberOfCycles,
                colors = transparentButtonTextColors(),
                shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small)
            ) {
                Text(
                    text = stringResource(id = R.string.plus),
                    fontWeight = FontWeight.Bold,
                    fontSize = 44.sp,
                )
            }
        }
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.total_length, totalLengthFormatted),
            style = MaterialTheme.typography.headlineSmall
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
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun NumberCyclesComponentPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            NumberCyclesComponent(
                numberOfCycles = 3,
                lengthOfCycle = "4mn",
                totalLengthFormatted = "20mn"
            )
        }
    }
}

