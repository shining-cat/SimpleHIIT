package fr.shiningcat.simplehiit.domain.common

import fr.shiningcat.simplehiit.domain.common.models.DomainError

sealed interface Output<out T> {
    data class Success<out T>(
        val result: T,
    ) : Output<T>

    data class Error(
        val errorCode: DomainError,
        val exception: Throwable,
    ) : Output<Nothing> {
        override fun toString() = "errorCode: $errorCode | exception: $exception"
    }
}
