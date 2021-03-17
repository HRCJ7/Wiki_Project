package com.example.mapapplication.utils.network

interface NetworkHelper {

    fun isNetworkConnected(): Boolean

    fun castToNetworkError(throwable: Throwable): NetworkError

}