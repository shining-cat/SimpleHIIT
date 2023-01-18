package fr.shining_cat.simplehiit.utils

import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
interface HiitLogger {

    fun d(tag: String, msg: String)

    fun e(tag: String, msg: String, throwable: Throwable? = null)
}