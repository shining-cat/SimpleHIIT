/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.session.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import fr.shiningcat.simplehiit.domain.common.models.WorkPeriodPosition
import fr.shiningcat.simplehiit.sharedui.session.RunningSessionStepType
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun SessionProgressIndicatorComponent(
    position: WorkPeriodPosition,
    periodType: RunningSessionStepType,
    modifier: Modifier = Modifier,
) {
    val visible =
        stringResource(
            id = CommonResourcesR.string.session_progress_period_cycle,
            position.workPeriodInCycle,
            position.totalWorkPeriodsInCycle,
            position.cycle,
            position.totalCycles,
        )
    val a11yRes =
        when (periodType) {
            RunningSessionStepType.WORK -> CommonResourcesR.string.session_progress_a11y_work
            RunningSessionStepType.REST -> CommonResourcesR.string.session_progress_a11y_rest
        }
    val a11y =
        stringResource(
            id = a11yRes,
            position.workPeriodInCycle,
            position.totalWorkPeriodsInCycle,
            position.cycle,
            position.totalCycles,
        )
    Text(
        text = visible,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground,
        modifier =
            modifier
                .fillMaxWidth()
                .clearAndSetSemantics { contentDescription = a11y },
    )
}
