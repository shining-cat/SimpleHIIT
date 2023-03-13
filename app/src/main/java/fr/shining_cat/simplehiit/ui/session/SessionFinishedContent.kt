package fr.shining_cat.simplehiit.ui.session

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme

@Composable
fun SessionFinishedContent(viewState: SessionViewState.SessionFinished) {
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        item {
            SessionFinishedHeaderComponent(viewState.sessionDurationFormatted)
        }
        items(viewState.exercisesDone.size) {
            SessionFinishedExerciseDoneItemComponent(viewState.exercisesDone[it])
        }
    }
}

@Composable
fun SessionFinishedHeaderComponent(sessionDurationFormatted: String) {
    Column {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 48.dp),
            style = MaterialTheme.typography.headlineLarge,
            text = stringResource(id = R.string.finish_page_title)
        )
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(id = R.string.session_length_summary, sessionDurationFormatted)
        )
        Spacer(Modifier.fillMaxWidth().height(24.dp))
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineSmall,
            text = stringResource(id = R.string.summary_session)
        )
        Spacer(Modifier.fillMaxWidth().height(16.dp))
    }
}

@Composable
fun SessionFinishedExerciseDoneItemComponent(exerciseDone: String) {
    Text(
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal= 16.dp),
        style = MaterialTheme.typography.bodyMedium,
        text = exerciseDone
    )
}

// Previews
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SessionFinishedContentPreview(
    @PreviewParameter(SessionFinishedContentPreviewParameterProvider::class) viewState: SessionViewState.SessionFinished
) {
    SimpleHiitTheme {
        SessionFinishedContent(
            viewState = viewState
        )
    }
}

internal class SessionFinishedContentPreviewParameterProvider :
    PreviewParameterProvider<SessionViewState.SessionFinished> {
    override val values: Sequence<SessionViewState.SessionFinished>
        get() = sequenceOf(
            SessionViewState.SessionFinished(
                sessionDurationFormatted = "3mn",
                exercisesDone = listOf(
                    "Lunges : Alternate to the Front Raising Arms in Front",
                    "Lunges : Alternate to the Back Squeezing Shoulder Blades"
                )
            ),
            SessionViewState.SessionFinished(
                sessionDurationFormatted = "25mn 30s",
                exercisesDone = listOf(
                    "Lunges : Alternate to the Front Raising Arms in Front",
                    "Lunges : Alternate to the Back Squeezing Shoulder Blades",
                    "Lunges : Alternate Lunge and Back Kick One Leg",
                    "Lying : Superman Twist",
                    "Sitting : Hands on the Floor Fold and Extend Legs",
                    "Squat : Basic Squat",
                    "Standing : Alternate Raise Extended Leg to the Front and Touching Foot with Opposite Hand",
                    "Lunges : Alternate to the Front Raising Arms in Front",
                    "Lunges : Alternate to the Back Squeezing Shoulder Blades",
                    "Lunges : Alternate Lunge and Back Kick One Leg",
                    "Lying : Superman Twist",
                    "Sitting : Hands on the Floor Fold and Extend Legs",
                    "Squat : Basic Squat",
                    "Standing : Alternate Raise Extended Leg to the Front and Touching Foot with Opposite Hand",
                    "Lunges : Alternate to the Front Raising Arms in Front",
                    "Lunges : Alternate to the Back Squeezing Shoulder Blades",
                    "Lunges : Alternate Lunge and Back Kick One Leg",
                    "Lying : Superman Twist",
                    "Sitting : Hands on the Floor Fold and Extend Legs",
                    "Squat : Basic Squat",
                    "Standing : Alternate Raise Extended Leg to the Front and Touching Foot with Opposite Hand"
                )
            )
        )
}