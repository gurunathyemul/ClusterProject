package com.example.data

import com.example.domain.exceptions.ErrorModel
import com.example.domain.exceptions.IErrorHandler
import kotlinx.coroutines.CancellationException
import okhttp3.ResponseBody
import java.io.IOException
import java.net.SocketTimeoutException

class ErrorHandler : IErrorHandler {
    override fun handleException(throwable: Throwable?): ErrorModel {
        //retrofit exceptions
        val errorModel: ErrorModel? = when (throwable) {

            is retrofit2.HttpException -> {
                getHttpError(throwable.response()?.errorBody(), throwable.code())
            }

            is SocketTimeoutException -> {
                ErrorModel(throwable.message, ErrorModel.ErrorStatus.TIMEOUT)
            }

            // handle connection error
            is IOException -> {
                ErrorModel(throwable.message, ErrorModel.ErrorStatus.NO_CONNECTION)
            }

            is CancellationException -> {
                ErrorModel("Canceled by user!", 0, ErrorModel.ErrorStatus.CANCELED)
            }

            else -> null
        }
        return errorModel ?: ErrorModel(
            "${throwable?.message}",
            0,
            ErrorModel.ErrorStatus.BAD_RESPONSE
        )
    }

    /**
     * attempts to parse http response body and get error data from it
     *
     * @param body retrofit response body
     * @return returns an instance of [ErrorModel] with parsed data or NOT_DEFINED status
     */
    private fun getHttpError(body: ResponseBody?, code: Int): ErrorModel {
        return try {
            when (code) {
                401, 403 -> ErrorModel(body.toString(), code, ErrorModel.ErrorStatus.UNAUTHORIZED)
                404 -> ErrorModel(body.toString(), code, ErrorModel.ErrorStatus.EMPTY_RESPONSE)
                429 -> ErrorModel(body.toString(), code, ErrorModel.ErrorStatus.TOO_MANY_ATTEMPTS)
                in 500..599 -> ErrorModel(body.toString(), 500, ErrorModel.ErrorStatus.BAD_RESPONSE)
                else -> ErrorModel(body.toString(), ErrorModel.ErrorStatus.NOT_DEFINED)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            ErrorModel(message = e.message, errorStatus = ErrorModel.ErrorStatus.NOT_DEFINED)
        }
    }
}