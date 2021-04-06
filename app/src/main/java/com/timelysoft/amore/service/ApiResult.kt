package com.timelysoft.amore.service

import okhttp3.ResponseBody


sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val errorResponse: ResponseBody?) : ApiResult<Nothing>()
    data class NetworkError(val message: ErrorTypes) : ApiResult<Nothing>()
    data class Loading(val loading: String): ApiResult<Nothing>()
}
inline fun <T> ApiResult<T>.doIfError(callback: (errorResponse : ResponseBody?) -> Unit) {
    if (this is ApiResult.Error) {
        callback(errorResponse)
    }
}
inline fun <T> ApiResult<T>.doIfSuccess(callback: (value: T) -> Unit) {
    if (this is ApiResult.Success) {
        callback(data)
    }
}
inline fun <T> ApiResult<T>.doIfNetwork(callback: (value: ErrorTypes) -> Unit){
    if (this is ApiResult.NetworkError){
        callback(message)
    }
}
inline fun <T> ApiResult<T>.doIfLoading(callback: (value: String) -> Unit){
    if (this is ApiResult.Loading){
        callback(loading)
    }
}
sealed class ErrorTypes{
    data class TimeOutError(val msg:String):ErrorTypes()
    data class ConnectionError(val msg:String):ErrorTypes()
    data class EmptyResultError(val msg:String):ErrorTypes()
}