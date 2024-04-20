package com.example.data.remote.datasource

import com.example.domain.model.news.NewsHeadLineResponse


interface RemoteDataSource {
    suspend fun getNewsHeadLine(country:String):NewsHeadLineResponse
}