package fr.shining_cat.simplehiit.domain.common

import fr.shining_cat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport

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