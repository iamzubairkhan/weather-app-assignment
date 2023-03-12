package com.example.weatherappassignment.data.remote

sealed class ApiException(message: String, cause: Throwable? = null) : RuntimeException(message, cause) {
    class NetworkException(message: String, cause: Throwable? = null) : ApiException(message, cause)
    class ServerException(message: String, cause: Throwable? = null) : ApiException(message, cause)
    class ClientException(message: String, cause: Throwable? = null) : ApiException(message, cause)
    class UnknownException(message: String, cause: Throwable? = null) : ApiException(message, cause)
}