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
import androidx.hilt.navigation.compose.hiltViewModel
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
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger

@Composable
fun SessionScreen(
    navigateUp: () -> Boolean,
    uiArrangement: UiArrangement,
    hiitLogger: HiitLogger,
    viewModel: SessionViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    // Handling the sound loading in the viewModel's soundPool:
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if (viewModel.isSoundLoaded().not()) {
            hiitLogger.d("SessionScreen", "loading beep sound in SoundPool")
            // we want this loading to only happen once, to benefit from the pooling and avoid playback latency, but SideEffects wouldn't let us access the context we need
            viewModel.getSoundPool()?.load(context, R.raw.sound_beep, 0)
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
    val (title, buttonLabel, clickOnButton) =
        when (screenViewState) {
            is SessionViewState.RunningNominal -> {
                when (screenViewState.periodType) {
                    RunningSessionStepType.REST -> {
                        Triple(
                            R.string.session_rest_page_title,
                            R.string.pause,
                            pause,
                        )
                    }
                    RunningSessionStepType.WORK -> {
                        Triple(
                            R.string.session_work_page_title,
                            R.string.pause,
                            pause,
                        )
                    }
                }
            }
            is SessionViewState.InitialCountDownSession -> {
                Triple(
                    R.string.session_prepare_page_title,
                    R.string.back_button_content_label,
                    navigateUp,
                )
            }
            is SessionViewState.Finished -> {
                Triple(
                    R.string.finish_page_topbar,
                    R.string.back_button_content_label,
                    navigateUp,
                )
            }
            is SessionViewState.Error -> {
                Triple(
                    R.string.error,
                    R.string.back_button_content_label,
                    navigateUp,
                )
            }
            SessionViewState.Loading -> {
                Triple(
                    R.string.session_loading_page_topBar,
                    R.string.back_button_content_label,
                    navigateUp,
                )
            }
        }
    // manually building layout instead of using native Scaffold to get more flexibility:
    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = uiArrangement == UiArrangement.HORIZONTAL) {
            SessionSideBarComponent(
                title = title,
                onBackButtonClick = { clickOnButton.invoke() },
                backButtonLabel = buttonLabel,
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
                    navigateUp = {
                        clickOnButton.invoke()
                        true
                    },
                    title = title,
                    overrideBackLabel = buttonLabel,
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
