package com.example.noteautomatic.foundation.base

typealias Mapper<Input, Output> = (Input) -> Output

sealed class AppResult<T> {

    fun <R> map(mapper: Mapper<T, R>? = null): AppResult<R> = when (this) {
        is PendingResult -> PendingResult()
        is ErrorResult -> ErrorResult(this.exception)
        is SuccessResult -> {
            if (mapper == null) throw IllegalArgumentException("Mapper should not be NULL for success result")
            SuccessResult(mapper(this.data))
        }
    }

}

class PendingResult<T> : AppResult<T>()

class SuccessResult<T>(
    val data: T
) : AppResult<T>()

class ErrorResult<T>(
    val exception: Exception
) : AppResult<T>()

fun <T> AppResult<T>?.takeSuccess(): T? {
    return if (this is SuccessResult)
        this.data
    else
        null
}