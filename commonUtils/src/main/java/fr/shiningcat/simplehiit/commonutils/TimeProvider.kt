package fr.shiningcat.simplehiit.commonutils

import fr.shiningcat.simplehiit.commonutils.annotations.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
interface TimeProvider {
    fun getCurrentTimeMillis(): Long
}
