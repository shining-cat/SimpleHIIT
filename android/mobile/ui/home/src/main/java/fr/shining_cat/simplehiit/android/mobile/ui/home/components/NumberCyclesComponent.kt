package fr.shining_cat.simplehiit.android.mobile.ui.home.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.android.mobile.common.theme.SimpleHiitTheme

@Composable
fun NumberCyclesComponent(
    openInputNumberCycles: (Int) -> Unit,
    numberOfCycles: Int,
    lengthOfCycle: String
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { openInputNumberCycles(numberOfCycles) }
    ) {

        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            text = stringResource(id = R.string.number_of_cycle_setting_title)
        )
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(id = R.string.short_instruction)
        )
        Row(
            Modifier
                .padding(horizontal = 0.dp, vertical = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = numberOfCycles.toString(),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.number_of_cycle_setting, lengthOfCycle),
                style = MaterialTheme.typography.headlineMedium
            )
        }
        //TODO maybe? add mention to tell total length of session
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
private fun NumberCyclesComponentPreview() {
    SimpleHiitTheme {
        Surface {
            NumberCyclesComponent(
                openInputNumberCycles = {},
                numberOfCycles = 5,
                lengthOfCycle = "4mn"
            )
        }
    }
}

