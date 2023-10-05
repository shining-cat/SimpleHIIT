package fr.shiningcat.simplehiit.android.mobile.ui.session.contents

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.session.CountDown
import fr.shiningcat.simplehiit.android.mobile.ui.session.RunningSessionStepType
import fr.shiningcat.simplehiit.android.mobile.ui.session.SessionViewState
import fr.shiningcat.simplehiit.android.mobile.ui.session.components.ExerciseDisplayComponent
import fr.shiningcat.simplehiit.android.mobile.ui.session.components.RunningSessionStepInfoDisplayComponent
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.AsymmetricalExerciseSideOrder
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide

@Composable
fun SessionRunningNominalContent(
    uiArrangement: UiArrangement,
    viewState: SessionViewState.RunningNominal,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    val exercise =
        viewState.displayedExercise // this avoids the Gif display to be recomposed between rest and work period, and avoids a jump in the loop
    val periodType = viewState.periodType // no need to recompose this every second either
    val exerciseSide = viewState.side // no need to recompose this every second either
    when (uiArrangement) {
        UiArrangement.VERTICAL -> VerticalSessionRunningNominalContent(
            exercise = exercise,
            periodType = periodType,
            exerciseSide = exerciseSide,
            countDown = viewState.countDown,
            viewState = viewState,
            hiitLogger = hiitLogger
        )

        UiArrangement.HORIZONTAL -> HorizontalSessionRunningNominalContent(
            exercise = exercise,
            periodType = periodType,
            exerciseSide = exerciseSide,
            countDown = viewState.countDown,
            viewState = viewState,
            hiitLogger = hiitLogger
        )
    }
}

@Composable
fun VerticalSessionRunningNominalContent(
    exercise: Exercise,
    periodType: RunningSessionStepType,
    exerciseSide: ExerciseSide,
    countDown: CountDown? = null,
    viewState: SessionViewState.RunningNominal,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExerciseDisplayComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 24.dp),
            exercise = exercise,
            periodType = periodType,
            exerciseSide = exerciseSide,
            countDown = countDown,
            hiitLogger = hiitLogger
        )
        RunningSessionStepInfoDisplayComponent(
            exercise = exercise,
            periodType = periodType,
            exerciseSide = exerciseSide,
            viewState = viewState,
            hiitLogger = hiitLogger
        )
    }
}

@Composable
fun HorizontalSessionRunningNominalContent(
    exercise: Exercise,
    periodType: RunningSessionStepType,
    exerciseSide: ExerciseSide,
    countDown: CountDown? = null,
    viewState: SessionViewState.RunningNominal,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null
) {
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
            countDown = countDown,
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
@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    widthDp = 400
)
@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 400
)
@Composable
private fun SessionRunningNominalContentPreviewPhonePortrait(
    @PreviewParameter(SessionRunningNominalContentPreviewParameterProvider::class) viewState: SessionViewState.RunningNominal
) {
    SimpleHiitMobileTheme {
        Surface {
            SessionRunningNominalContent(
                uiArrangement = UiArrangement.VERTICAL,
                viewState = viewState
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.TABLET,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showSystemUi = true,
    device = Devices.TABLET,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SessionRunningNominalContentPreviewTabletLandscape(
    @PreviewParameter(SessionRunningNominalContentPreviewParameterProvider::class) viewState: SessionViewState.RunningNominal
) {
    SimpleHiitMobileTheme {
        Surface {
            SessionRunningNominalContent(
                uiArrangement = UiArrangement.HORIZONTAL,
                viewState = viewState
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    device = "spec:parent=pixel_4,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    heightDp = 400
)
@Preview(
    showSystemUi = true,
    device = "spec:parent=pixel_4,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    heightDp = 400
)
@Composable
private fun SessionRunningNominalContentPreviewPhoneLandscape(
    @PreviewParameter(SessionRunningNominalContentPreviewParameterProvider::class) viewState: SessionViewState.RunningNominal
) {
    SimpleHiitMobileTheme {
        Surface {
            SessionRunningNominalContent(
                uiArrangement = UiArrangement.HORIZONTAL,
                viewState = viewState
            )
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