@file:KeepForCompose

package fr.shiningcat.simplehiit.android.mobile.ui.session

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.NavigateUpTopBar
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.mainContentInsets
import fr.shiningcat.simplehiit.android.mobile.ui.session.components.SessionSideBarComponent
import fr.shiningcat.simplehiit.android.mobile.ui.session.contents.SessionContentHolder
import fr.shiningcat.simplehiit.android.shared.session.SessionViewModel
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.annotations.KeepForCompose
import fr.shiningcat.simplehiit.sharedui.session.RunningSessionStepType
import fr.shiningcat.simplehiit.sharedui.session.SessionDialog
import fr.shiningcat.simplehiit.sharedui.session.SessionViewState
import org.koin.androidx.compose.koinViewModel

@Composable
fun SessionScreen(
    navigateUp: () -> Boolean,
    uiArrangement: UiArrangement,
    hiitLogger: HiitLogger,
    viewModel: SessionViewModel = koinViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    // Handling the sound loading in the viewModel's soundPool:
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if (viewModel.isSoundLoaded().not()) {
            hiitLogger.d("SessionScreen", "loading beep sound in SoundPool")
            // we want this loading to only happen once, to benefit from the pooling and avoid playback latency, but SideEffects wouldn't let us access the context we need
            viewModel.getSoundPool().load(context, R.raw.sound_beep, 0)
        } else {
            // Sound already loaded (from previous session with same ViewModel), directly trigger initialization
            hiitLogger.d("SessionScreen", "sound already loaded, reinitializing session")
            viewModel.reinitializeSession()
        }
    }
    // Setting up a LifeCycle observer to catch the onPause event of the Android Lifecycle so we can pause the session
    var lifecycleEvent by remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver =
            LifecycleEventObserver { _, event ->
                lifecycleEvent = event
            }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            // Don't forget to clean up the LifeCycle observer when we're disposed
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    val screenViewState by viewModel.screenViewState.collectAsStateWithLifecycle()
    val dialogViewState by viewModel.dialogViewState.collectAsStateWithLifecycle()

    LaunchedEffect(lifecycleEvent, screenViewState) {
        if (lifecycleEvent == Lifecycle.Event.ON_PAUSE &&
            (
                screenViewState is SessionViewState.RunningNominal ||
                    screenViewState is SessionViewState.InitialCountDownSession
            )
        ) {
            // only trigger pause session if we're actually running one
            viewModel.pause()
        }
    }
    //
    SessionScreen(
        onAbortSession = { viewModel.abortSession() },
        uiArrangement = uiArrangement,
        dialogViewState = dialogViewState,
        screenViewState = screenViewState,
        pause = { viewModel.pause() },
        resume = { viewModel.resume() },
        navigateUp = navigateUp,
        hiitLogger = hiitLogger,
    )
}

@Composable
fun SessionScreen(
    navigateUp: () -> Boolean = { true },
    onAbortSession: () -> Unit = {},
    pause: () -> Unit = {},
    resume: () -> Unit = {},
    uiArrangement: UiArrangement,
    dialogViewState: SessionDialog,
    screenViewState: SessionViewState,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    BackHandler(enabled = screenViewState is SessionViewState.RunningNominal) {
        pause()
    }
    val (icon, label, clickOnButton) =
        when (screenViewState) {
            is SessionViewState.RunningNominal -> {
                when (screenViewState.periodType) {
                    RunningSessionStepType.REST,
                    RunningSessionStepType.WORK,
                    -> {
                        Triple(
                            R.drawable.pause,
                            R.string.pause,
                            pause,
                        )
                    }
                }
            }
            is SessionViewState.InitialCountDownSession,
            is SessionViewState.Finished,
            is SessionViewState.Error,
            SessionViewState.Loading,
            -> {
                Triple(
                    R.drawable.arrow_back,
                    R.string.back_button_content_label,
                    navigateUp,
                )
            }
        }
    // manually building layout instead of using native Scaffold to get more flexibility:
    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = uiArrangement == UiArrangement.HORIZONTAL) {
            SessionSideBarComponent(
                icon = icon,
                onClick = { clickOnButton.invoke() },
                label = label,
            )
        }
        Column(
            modifier =
                Modifier
                    .mainContentInsets(uiArrangement)
                    .fillMaxSize(),
        ) {
            AnimatedVisibility(visible = uiArrangement == UiArrangement.VERTICAL) {
                NavigateUpTopBar(
                    icon = icon,
                    label = label,
                    onClick = {
                        clickOnButton.invoke()
                        true
                    },
                )
            }
            SessionContentHolder(
                dialogViewState = dialogViewState,
                screenViewState = screenViewState,
                uiArrangement = uiArrangement,
                onAbortSession = onAbortSession,
                onResume = resume,
                navigateUp = navigateUp,
                hiitLogger = hiitLogger,
            )
        }
    }
}
