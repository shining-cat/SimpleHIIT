package fr.shiningcat.simplehiit.android.tv.ui.session.contents

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.session.SessionViewState
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonresources.helpers.ExerciseDisplayNameMapper
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide
import fr.shiningcat.simplehiit.domain.common.models.SessionStepDisplay

@Composable
fun SessionFinishedContent(
    viewState: SessionViewState.Finished,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    LazyColumn(
        modifier =
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        item {
            SessionFinishedHeaderComponent(viewState.sessionDurationFormatted)
        }
        val exerciseNameResMapper = ExerciseDisplayNameMapper()
        items(viewState.workingStepsDone.size) {
            val step = viewState.workingStepsDone[it]
            val exerciseNameRes =
                exerciseNameResMapper.map(step.exercise)
            SessionFinishedExerciseDoneItemComponent(exerciseNameRes, step.side)
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SessionFinishedHeaderComponent(sessionDurationFormatted: String) {
    Column {
        Text(
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp),
            style = MaterialTheme.typography.headlineLarge,
            text = stringResource(id = R.string.finish_page_title),
        )
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(id = R.string.session_length_summary, sessionDurationFormatted),
        )
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(24.dp),
        )
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineSmall,
            text = stringResource(id = R.string.session_finished_tips),
        )
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(16.dp),
        )
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineSmall,
            text = stringResource(id = R.string.summary_session),
        )
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(16.dp),
        )
    }
}

@Composable
fun SessionFinishedExerciseDoneItemComponent(
    exerciseDoneRes: Int,
    side: ExerciseSide,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 16.dp),
    ) {
        var displayText = stringResource(id = exerciseDoneRes)
        val displaySideRes =
            when (side) {
                ExerciseSide.NONE -> null
                ExerciseSide.LEFT -> R.string.exercise_side_left
                ExerciseSide.RIGHT -> R.string.exercise_side_right
            }
        if (displaySideRes != null) {
            displayText += " - " + stringResource(id = displaySideRes)
        }
        Text(
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium,
            text = displayText,
        )
    }
}

// Previews
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun SessionFinishedContentPreview(
    @PreviewParameter(SessionFinishedContentPreviewParameterProvider::class) viewState: SessionViewState.Finished,
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            SessionFinishedContent(viewState = viewState)
        }
    }
}

internal class SessionFinishedContentPreviewParameterProvider : PreviewParameterProvider<SessionViewState.Finished> {
    override val values: Sequence<SessionViewState.Finished>
        get() =
            sequenceOf(
                SessionViewState.Finished(
                    sessionDurationFormatted = "3mn",
                    workingStepsDone =
                        listOf(
                            SessionStepDisplay(Exercise.CrabAdvancedBridge, ExerciseSide.NONE),
                            SessionStepDisplay(Exercise.CatDonkeyKickTwist, ExerciseSide.NONE),
                        ),
                ),
                SessionViewState.Finished(
                    sessionDurationFormatted = "25mn 30s",
                    workingStepsDone =
                        listOf(
                            SessionStepDisplay(Exercise.CatBackLegLift, ExerciseSide.NONE),
                            SessionStepDisplay(Exercise.CatKneePushUp, ExerciseSide.NONE),
                            SessionStepDisplay(Exercise.LungesArmsCrossSide, ExerciseSide.LEFT),
                            SessionStepDisplay(Exercise.LungesArmsCrossSide, ExerciseSide.RIGHT),
                            SessionStepDisplay(Exercise.LungesTwist, ExerciseSide.NONE),
                            SessionStepDisplay(Exercise.LyingStarToeTouchSitUp, ExerciseSide.NONE),
                            SessionStepDisplay(Exercise.LyingSupermanTwist, ExerciseSide.NONE),
                            SessionStepDisplay(Exercise.StandingMountainClimber, ExerciseSide.NONE),
                            SessionStepDisplay(Exercise.PlankMountainClimber, ExerciseSide.LEFT),
                            SessionStepDisplay(Exercise.PlankMountainClimber, ExerciseSide.RIGHT),
                            SessionStepDisplay(Exercise.StandingKickCrunches, ExerciseSide.NONE),
                            SessionStepDisplay(Exercise.SquatBasic, ExerciseSide.NONE),
                            SessionStepDisplay(Exercise.PlankShoulderTap, ExerciseSide.NONE),
                            SessionStepDisplay(Exercise.PlankBirdDogs, ExerciseSide.NONE),
                        ),
                ),
            )
}
