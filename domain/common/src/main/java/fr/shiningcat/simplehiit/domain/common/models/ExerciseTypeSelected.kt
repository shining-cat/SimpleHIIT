package fr.shiningcat.simplehiit.domain.common.models

import fr.shiningcat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
data class ExerciseTypeSelected(
    val type: ExerciseType,
    val selected:Boolean
)
