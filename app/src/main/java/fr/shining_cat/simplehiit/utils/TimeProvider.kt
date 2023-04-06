package fr.shining_cat.simplehiit.utils

import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
interface TimeProvider {
    fun getCurrentTimeMillis():Long
}