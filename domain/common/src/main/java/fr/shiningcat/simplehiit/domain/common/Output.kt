package fr.shiningcat.simplehiit.domain.common

import fr.shiningcat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
sealed interface Output<out T> {

    @ExcludeFromJacocoGeneratedReport
    data class Success<out T>(
        val result: T
    ) : Output<T>

    @ExcludeFromJacocoGeneratedReport
    data class Error(
        val errorCode: Constants.Errors,
        val exception: Throwable
    ) : Output<Nothing> {

        override fun toString() =
            "errorCode: $errorCode | exception: $exception"
    }

}