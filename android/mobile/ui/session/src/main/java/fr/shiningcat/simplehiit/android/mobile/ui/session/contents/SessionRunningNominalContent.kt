package fr.shiningcat.simplehiit.android.mobile.ui.session.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.session.CountDown
import fr.shiningcat.simplehiit.android.mobile.ui.session.RunningSessionStepType
import fr.shiningcat.simplehiit.android.mobile.ui.session.SessionViewState
import fr.shiningcat.simplehiit.android.mobile.ui.session.components.ExerciseDisplayComponent
import fr.shiningcat.simplehiit.android.mobile.ui.session.components.RunningSessionStepInfoDisplayComponent
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.AsymmetricalExerciseSideOrder
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide

@Composable
fun SessionRunningNominalContent(
    modifier: Modifier = Modifier,
    uiArrangement: UiArrangement,
    viewState: SessionViewState.RunningNominal,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    // this avoids the Gif display to be recomposed between rest and work period, and avoids a jump in the loop
    val exercise = viewState.displayedExercise
    // no need to recompose this every second either
    val periodType = viewState.periodType
    // no need to recompose this every second either
    val exerciseSide = viewState.side
    when (uiArrangement) {
        UiArrangement.VERTICAL ->
            VerticalSessionRunningNominalContent(
                modifier = modifier,
                exercise = exercise,
                periodType = periodType,
                exerciseSide = exerciseSide,
                countDown = viewState.countDown,
                viewState = viewState,
                hiitLogger = hiitLogger,
            )

        UiArrangement.HORIZONTAL ->
            HorizontalSessionRunningNominalContent(
                modifier = modifier,
                exercise = exercise,
                periodType = periodType,
                exerciseSide = exerciseSide,
                countDown = viewState.countDown,
                viewState = viewState,
                hiitLogger = hiitLogger,
            )
    }
}

@Composable
fun VerticalSessionRunningNominalContent(
    modifier: Modifier = Modifier,
    exercise: Exercise,
    periodType: RunningSessionStepType,
    exerciseSide: ExerciseSide,
    countDown: CountDown? = null,
    viewState: SessionViewState.RunningNominal,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    Column(
        modifier =
            modifier
                .padding(dimensionResource(R.dimen.spacing_1))
                .windowInsetsPadding(WindowInsets.safeDrawing),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ExerciseDisplayComponent(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(R.dimen.spacing_1),
                        vertical = dimensionResource(R.dimen.spacing_3)
                    ),
            exercise = exercise,
            exerciseSide = exerciseSide,
            countDown = countDown,
            hiitLogger = hiitLogger,
        )
        RunningSessionStepInfoDisplayComponent(
            exercise = exercise,
            periodType = periodType,
            exerciseSide = exerciseSide,
            viewState = viewState,
            hiitLogger = hiitLogger,
        )
    }
}

@Composable
fun HorizontalSessionRunningNominalContent(
    modifier: Modifier = Modifier,
    exercise: Exercise,
    periodType: RunningSessionStepType,
    exerciseSide: ExerciseSide,
    countDown: CountDown? = null,
    viewState: SessionViewState.RunningNominal,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    hiitLogger?.d("HorizontalSessionRunningNominalContent", "ready")
    var availableHeight by remember { mutableIntStateOf(0) }
    Row(
        modifier =
            modifier
                .padding(dimensionResource(R.dimen.spacing_1))
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .onGloballyPositioned { coordinates ->
                    availableHeight = coordinates.size.height
                    hiitLogger?.d("ExerciseDisplayComponent", "availableHeight = $availableHeight")
                },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ExerciseDisplayComponent(
            modifier =
                Modifier
                    .weight(1f)
                    .height(availableHeight.dp)
                    .aspectRatio(1f)
                    .align(Alignment.CenterVertically),
            exercise = exercise,
            exerciseSide = exerciseSide,
            countDown = countDown,
            hiitLogger = hiitLogger,
        )
        RunningSessionStepInfoDisplayComponent(
            modifier = Modifier.weight(1f),
            exercise = exercise,
            periodType = periodType,
            exerciseSide = exerciseSide,
            viewState = viewState,
            hiitLogger = hiitLogger,
        )
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun SessionRunningNominalContentPreview(
    @PreviewParameter(SessionRunningNominalContentPreviewParameterProvider::class) viewState: SessionViewState.RunningNominal,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val previewUiArrangement: UiArrangement =
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) { // typically, a tablet or bigger in landscape
            UiArrangement.HORIZONTAL
        } else { // WindowWidthSizeClass.Medium, WindowWidthSizeClass.Compact :
            if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) { // typically, a phone in landscape
                UiArrangement.HORIZONTAL
            } else {
                UiArrangement.VERTICAL // typically, a phone or tablet in portrait
            }
        }
    SimpleHiitMobileTheme {
        Surface {
            SessionRunningNominalContent(
                modifier = Modifier.fillMaxSize(),
                uiArrangement = previewUiArrangement,
                viewState = viewState,
            )
        }
    }
}

internal class SessionRunningNominalContentPreviewParameterProvider : PreviewParameterProvider<SessionViewState.RunningNominal> {
    override val values: Sequence<SessionViewState.RunningNominal>
        get() =
            sequenceOf(
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.REST,
                    displayedExercise = Exercise.LungesSideToCurtsy,
                    side = AsymmetricalExerciseSideOrder.SECOND.side,
                    stepRemainingTime = "25s",
                    stepRemainingPercentage = .2f,
                    sessionRemainingTime = "3mn 25s",
                    sessionRemainingPercentage = .75f,
                    countDown = null,
                ),
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.WORK,
                    displayedExercise = Exercise.SquatBasic,
                    side = AsymmetricalExerciseSideOrder.SECOND.side,
                    stepRemainingTime = "3s",
                    stepRemainingPercentage = .02f,
                    sessionRemainingTime = "3mn 3s",
                    sessionRemainingPercentage = .745f,
                    countDown =
                        CountDown(
                            secondsDisplay = "3",
                            progress = .2f,
                            playBeep = true,
                        ),
                ),
            )
}
