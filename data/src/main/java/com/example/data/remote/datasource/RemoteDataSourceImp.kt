package com.example.data.remote.datasource

import com.example.data.remote.apiservices.NewsHeadlineApiService
import com.example.domain.model.news.NewsHeadLineResponse
import javax.inject.Inject

class RemoteDataSourceImp @Inject constructor(
    private val newsHeadlineApiService: NewsHeadlineApiService
) :
    RemoteDataSource {
    override suspend fun getNewsHeadLine(country: String): NewsHeadLineResponse {
        return newsHeadlineApiService.getNewsHeadline(country)
    }
}