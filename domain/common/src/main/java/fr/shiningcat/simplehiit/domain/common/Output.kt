package fr.shiningcat.simplehiit.domain.common

sealed interface Output<out T> {
    data class Success<out T>(
        val result: T,
    ) : Output<T>

    data class Error(
        val errorCode: Constants.Errors,
        val exception: Throwable,
    ) : Output<Nothing> {
        override fun toString() = "errorCode: $errorCode | exception: $exception"
    }
}
