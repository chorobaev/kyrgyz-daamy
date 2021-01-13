package com.timelysoft.kainarapp.service


data class Resource<out T>(val status: Status, val data: T?, val msg: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data : T?=null): Resource<T> {
            return Resource(Status.ERROR,data, msg)
        }

        fun <T> network(msg: String): Resource<T> {
            return Resource(Status.NETWORK,null, msg)
        }

        fun <T> empty(): Resource<T> {
            return Resource(Status.EMPTY, null,null)
        }

    }
}

enum class Status {
    SUCCESS,
    ERROR,
    NETWORK,
    EMPTY
}
