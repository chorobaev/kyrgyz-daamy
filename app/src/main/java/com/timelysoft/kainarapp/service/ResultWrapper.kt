package com.timelysoft.kainarapp.service

import com.timelysoft.kainarapp.service.model2.ErrorResponse

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value : T) : ResultWrapper<T>()
    data class GenericError(val code: Int?= null,val error :ErrorResponse ?= null):ResultWrapper<Nothing>()
}