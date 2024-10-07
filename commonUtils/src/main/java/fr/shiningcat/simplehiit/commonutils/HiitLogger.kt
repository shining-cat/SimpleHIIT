package fr.shiningcat.simplehiit.commonutils

import fr.shiningcat.simplehiit.commonutils.annotations.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
interface HiitLogger {
    fun d(
        tag: String,
        msg: String,
    )

    fun e(
        tag: String,
        msg: String,
        throwable: Throwable? = null,
    )
}
