package fr.shining_cat.simplehiit.commonutils

@ExcludeFromJacocoGeneratedReport
interface HiitLogger {

    fun d(tag: String, msg: String)

    fun e(tag: String, msg: String, throwable: Throwable? = null)
}