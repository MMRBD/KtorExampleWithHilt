package com.mmrbd.ktorexample.data.network

sealed class ApiSate {
    object Empty : ApiSate()
    object Loading : ApiSate()
    class Success<T>(val data: T) : ApiSate()
    class Failed(val message: Throwable) : ApiSate()
}