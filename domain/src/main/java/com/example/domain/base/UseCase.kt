package com.example.domain.base

import android.util.Log
import com.example.domain.exceptions.IErrorHandler
import java.util.concurrent.CancellationException

abstract class UseCase<Response, Params>(private val errorHandler: IErrorHandler) {

    abstract suspend fun run(params: Params? = null): Response

    suspend fun call(params: Params? = null, onResult: (UseCaseCallback<Response>)?) {
        try {
            val result = run(params)
            Log.d(TAG, "Response: $result")
            onResult?.onSuccess(result)
        } catch (e: CancellationException) {
            Log.d(TAG, "Error:$e")
            onResult?.onError(errorHandler.handleException(e))
        } catch (e: Exception) {
            Log.d(TAG, "Error:$e Cause:${e.cause}")
            onResult?.onError(errorHandler.handleException(e))
        }
    }

    companion object {
        private const val TAG = "UseCase"
    }
}