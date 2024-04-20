package com.example.domain.exceptions

fun interface IErrorHandler {
    fun handleException(throwable: Throwable?): ErrorModel
}