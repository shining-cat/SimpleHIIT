package fr.shining_cat.simplehiit.android.tv.ui.session.contents

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.android.tv.ui.session.CountDown
import fr.shining_cat.simplehiit.android.tv.ui.session.RunningSessionStepType
import fr.shining_cat.simplehiit.android.tv.ui.session.SessionViewState
import fr.shining_cat.simplehiit.android.tv.ui.session.components.ExerciseDisplayComponent
import fr.shining_cat.simplehiit.android.tv.ui.session.components.RunningSessionStepInfoDisplayComponent
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.domain.common.models.AsymmetricalExerciseSideOrder
import fr.shining_cat.simplehiit.domain.common.models.Exercise

@Composable
fun SessionRunningNominalContent(
    viewState: SessionViewState.RunningNominal,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    val exercise =
        viewState.displayedExercise // this avoids the Gif display to be recomposed between rest and work period, and avoids a jump in the loop
    val periodType = viewState.periodType // no need to recompose this every second either
    val exerciseSide = viewState.side // no need to recompose this every second either

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        ExerciseDisplayComponent(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            exercise = exercise,
            periodType = periodType,
            exerciseSide = exerciseSide,
            countDown = viewState.countDown,
            hiitLogger = hiitLogger
        )
        RunningSessionStepInfoDisplayComponent(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            exercise = exercise,
            periodType = periodType,
            exerciseSide = exerciseSide,
            viewState = viewState,
            hiitLogger = hiitLogger
        )
    }
}

// Previews
@OptIn(ExperimentalTvMaterial3Api::class)
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
private fun SessionRunningNominalContentPreview(
    @PreviewParameter(SessionRunningNominalContentPreviewParameterProvider::class) viewState: SessionViewState.RunningNominal
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            SessionRunningNominalContent(viewState = viewState)
        }
    }
}

internal class SessionRunningNominalContentPreviewParameterProvider :
    PreviewParameterProvider<SessionViewState.RunningNominal> {
    override val values: Sequence<SessionViewState.RunningNominal>
        get() = sequenceOf(
            SessionViewState.RunningNominal(
                periodType = RunningSessionStepType.REST,
                displayedExercise = Exercise.LungesSideToCurtsy,
                side = AsymmetricalExerciseSideOrder.SECOND.side,
                stepRemainingTime = "25s",
                stepRemainingPercentage = .2f,
                sessionRemainingTime = "3mn 25s",
                sessionRemainingPercentage = .75f,
                countDown = null
            ),
            SessionViewState.RunningNominal(
                periodType = RunningSessionStepType.WORK,
                displayedExercise = Exercise.LungesSideToCurtsy,
                side = AsymmetricalExerciseSideOrder.SECOND.side,
                stepRemainingTime = "3s",
                stepRemainingPercentage = .02f,
                sessionRemainingTime = "3mn 3s",
                sessionRemainingPercentage = .745f,
                countDown = CountDown(
                    secondsDisplay = "3",
                    progress = .2f,
                    playBeep = true
                )
            )
        )
}