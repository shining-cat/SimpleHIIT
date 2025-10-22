package fr.shiningcat.simplehiit.commonutils

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
