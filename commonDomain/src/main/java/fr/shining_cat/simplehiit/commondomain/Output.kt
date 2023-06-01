package fr.shining_cat.simplehiit.commondomain

import fr.shining_cat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
sealed class Output<out T> {

    @ExcludeFromJacocoGeneratedReport
    data class Success<out T>(
        val result: T
    ) : Output<T>()

    @ExcludeFromJacocoGeneratedReport
    data class Error(
        val errorCode: Constants.Errors,
        val exception: Throwable
    ) : Output<Nothing>() {

        override fun toString() =
            "errorCode: $errorCode | exception: $exception"
    }

}