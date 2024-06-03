package com.vinib.githubusers.data.util.request

sealed class ServiceResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T> : ServiceResult<T>()
    class Empty<T> : ServiceResult<T>()
    class Success<T>(data: T?) : ServiceResult<T>(data)
    class Error<T>(data: T? = null, message: String) : ServiceResult<T>(data, message)
}