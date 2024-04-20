package com.example.domain.base

import com.example.domain.exceptions.ErrorModel

interface UseCaseCallback<Type> {
    fun onSuccess(result: Type)
    fun onError(errorModel: ErrorModel?)
}