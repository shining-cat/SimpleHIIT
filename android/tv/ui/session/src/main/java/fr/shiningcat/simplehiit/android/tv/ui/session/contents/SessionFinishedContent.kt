package fr.shiningcat.simplehiit.android.tv.ui.session.contents

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreens
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonresources.helpers.ExerciseDisplayNameMapper
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide
import fr.shiningcat.simplehiit.domain.common.models.SessionStepDisplay
import fr.shiningcat.simplehiit.sharedui.session.SessionViewState

@Composable
fun SessionFinishedContent(
    viewState: SessionViewState.Finished,
    navigateUp: () -> Boolean = { true },
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    Column(
        modifier =
            Modifier
                .padding(horizontal = dimensionResource(R.dimen.spacing_2))
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Header with congratulations and summary
        SessionFinishedHeaderComponent(viewState.sessionDurationFormatted)

        // Two-column grid with invisible focusable spacers interspersed to enable D-pad scrolling
        val exerciseNameResMapper = ExerciseDisplayNameMapper()
        val itemsPerSpacer = 6 // Add a focusable spacer every 6 items (3 rows in 2-column grid)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = dimensionResource(R.dimen.spacing_1)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_2)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_05)),
        ) {
            val exercises = viewState.workingStepsDone

            // Add invisible focusable spacer at the top
            item(span = { GridItemSpan(2) }) {
                Spacer(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.spacing_025))
                            .focusable(),
                )
            }

            exercises.forEachIndexed { index, step ->
                // Add exercise item with alternating row background
                item {
                    val rowIndex = index / 2
                    val hasBackground = rowIndex % 2 == 1
                    val exerciseNameRes = exerciseNameResMapper.map(step.exercise)
                    SessionFinishedExerciseDoneItemComponent(
                        exerciseIndex = index + 1,
                        exerciseNameRes = exerciseNameRes,
                        side = step.side,
                        hasBackground = hasBackground,
                    )
                }

                // Add invisible focusable spacer every few items to enable scrolling
                if ((index + 1) % itemsPerSpacer == 0 && index < exercises.size - 1) {
                    item(span = { GridItemSpan(2) }) {
                        Spacer(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(dimensionResource(R.dimen.spacing_025))
                                    .focusable(),
                        )
                    }
                }
            }

            // Add invisible focusable spacer at the bottom
            item(span = { GridItemSpan(2) }) {
                Spacer(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.spacing_025))
                            .focusable(),
                )
            }
        }
    }
}

@Composable
fun SessionFinishedHeaderComponent(sessionDurationFormatted: String) {
    Column(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_2))) {
        // Large congratulations message
        Text(
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.spacing_2)),
            style = MaterialTheme.typography.headlineLarge,
            text = stringResource(id = R.string.finish_page_title),
        )

        // Smaller session duration
        Text(
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.spacing_15)),
            style = MaterialTheme.typography.titleLarge,
            text = stringResource(id = R.string.session_length_summary, sessionDurationFormatted),
        )

        // Compact tips and summary label
        Text(
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.spacing_1)),
            style = MaterialTheme.typography.bodyLarge,
            text = stringResource(id = R.string.session_finished_tips),
        )

        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge,
            text = stringResource(id = R.string.summary_session),
        )
    }
}

@Composable
fun SessionFinishedExerciseDoneItemComponent(
    exerciseIndex: Int,
    exerciseNameRes: Int,
    side: ExerciseSide,
    hasBackground: Boolean = false,
) {
    var displayText = exerciseIndex.toString() + ":   " + stringResource(id = exerciseNameRes)
    val displaySideRes =
        when (side) {
            ExerciseSide.NONE -> null
            ExerciseSide.LEFT -> R.string.exercise_side_left
            ExerciseSide.RIGHT -> R.string.exercise_side_right
        }
    if (displaySideRes != null) {
        displayText += " - " + stringResource(id = displaySideRes)
    }

    val backgroundColor =
        if (hasBackground) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.background
        }

    Text(
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.bodySmall,
        text = displayText,
        modifier =
            Modifier
                .background(backgroundColor)
                .padding(dimensionResource(R.dimen.spacing_1)),
    )
}

// Previews
@ExperimentalTvMaterial3Api
@PreviewTvScreens
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
