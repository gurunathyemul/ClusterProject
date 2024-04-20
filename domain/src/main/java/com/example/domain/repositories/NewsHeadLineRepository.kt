package com.example.domain.repositories

import com.example.domain.model.news.NewsHeadLineResponse

fun interface NewsHeadLineRepository {
    suspend fun getNewsHeadline(country: String): NewsHeadLineResponse
}