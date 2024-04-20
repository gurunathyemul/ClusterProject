package com.example.data.repositories

import com.example.data.remote.datasource.RemoteDataSource
import com.example.domain.model.news.NewsHeadLineResponse
import com.example.domain.repositories.NewsHeadLineRepository
import javax.inject.Inject

class NewsHeadlineRepositoryImp @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    NewsHeadLineRepository {
    override suspend fun getNewsHeadline(country: String): NewsHeadLineResponse =
        remoteDataSource.getNewsHeadLine(country)

}