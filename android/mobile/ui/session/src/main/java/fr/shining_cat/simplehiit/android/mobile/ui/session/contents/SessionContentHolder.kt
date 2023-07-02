package fr.shining_cat.simplehiit.android.mobile.ui.session.contents

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import fr.shining_cat.simplehiit.android.mobile.ui.common.components.BasicLoading
import fr.shining_cat.simplehiit.android.mobile.ui.common.components.ChoiceDialog
import fr.shining_cat.simplehiit.android.mobile.ui.session.SessionDialog
import fr.shining_cat.simplehiit.android.mobile.ui.session.SessionViewState
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.commonutils.HiitLogger

@Composable
fun SessionContentHolder(
    dialogViewState: SessionDialog,
    screenViewState: SessionViewState,
    onAbortSession: () -> Unit,
    resume: () -> Unit,
    navigateUp: () -> Boolean,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null
) {
    when (screenViewState) {
        SessionViewState.Loading -> BasicLoading()

        is SessionViewState.Error -> SessionErrorStateContent(
            screenViewState = screenViewState,
            navigateUp = navigateUp,
            onAbort = onAbortSession,
            hiitLogger = hiitLogger
        )

        is SessionViewState.InitialCountDownSession -> SessionPrepareContent(
            viewState = screenViewState,
            hiitLogger = hiitLogger
        )

        is SessionViewState.RestNominal -> SessionRestNominalContent(
            viewState = screenViewState,
            hiitLogger = hiitLogger
        )

        is SessionViewState.WorkNominal -> SessionWorkNominalContent(
            viewState = screenViewState,
            hiitLogger = hiitLogger
        )

        is SessionViewState.Finished -> {
            if (screenViewState.workingStepsDone.isEmpty()) {
                navigateUp()
            } else {
                SessionFinishedContent(
                    viewState = screenViewState,
                    hiitLogger = hiitLogger
                )
            }
        }
    }
    when (dialogViewState) {
        SessionDialog.None -> {}/*Do nothing*/
        SessionDialog.Pause -> ChoiceDialog(
            title = stringResource(id = R.string.pause),
            message = stringResource(id = R.string.pause_explanation),
            primaryButtonLabel = stringResource(id = R.string.resume_button_label),
            primaryAction = resume,
            secondaryButtonLabel = stringResource(R.string.abort_session_button_label),
            secondaryAction = onAbortSession,
            dismissAction = resume
        )
    }
}