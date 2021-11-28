package com.veronica.idn.githubappsecondsub.domain.data.network

// R -> result keseluruhan
// T -> data
open class ApiResult<out R> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val throwable: Throwable) : ApiResult<Nothing>()
}