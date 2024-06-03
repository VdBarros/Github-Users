package com.vinib.githubusers.data.util.request


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

open class RequestHandler {
    protected suspend fun <T> makeRequest(request: suspend () -> T): Flow<ServiceResult<T>> {
        return flow {
            emit(ServiceResult.Loading())
            val requestFromApi = try {
                request()
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ServiceResult.Error(message = "Ocorreu um erro Inesperado"))
                return@flow
            }
            emit(ServiceResult.Success(requestFromApi))
        }
    }
}