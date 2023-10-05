package fr.shiningcat.simplehiit.commonutils

@ExcludeFromJacocoGeneratedReport
interface HiitLogger {

    fun d(tag: String, msg: String)

    fun e(tag: String, msg: String, throwable: Throwable? = null)
}