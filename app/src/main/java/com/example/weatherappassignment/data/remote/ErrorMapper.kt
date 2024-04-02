package com.example.weatherappassignment.data.remote

import com.example.weatherappassignment.R
import com.example.weatherappassignment.data.remote.ApiException.ClientException
import com.example.weatherappassignment.data.remote.ApiException.NetworkException
import com.example.weatherappassignment.data.remote.ApiException.ServerException
import com.example.weatherappassignment.data.remote.ApiException.UnknownException
import com.example.weatherappassignment.utils.ResourceProvider
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

fun interface ErrorMapper {
    fun map(exception: Exception): ApiException
}

class ErrorMapperImpl @Inject constructor(private val resourceProvider: ResourceProvider) : ErrorMapper {
    override fun map(exception: Exception): ApiException {
        return when (exception) {
            is IOException -> NetworkException(resourceProvider.getString(R.string.network_error), exception)
            is HttpException -> handleHttpException(exception, resourceProvider)

            else -> UnknownException(resourceProvider.getString(R.string.unknown_error), exception)
        }
    }
}

fun handleHttpException(exception: HttpException, resourceProvider: ResourceProvider): ApiException {
    return when (exception.code()) {
        in 400..499 -> ClientException(resourceProvider.getString(R.string.client_error), exception)
        in 500..599 -> ServerException(resourceProvider.getString(R.string.server_error), exception)
        else -> UnknownException(resourceProvider.getString(R.string.unknown_error), exception)
    }
}
