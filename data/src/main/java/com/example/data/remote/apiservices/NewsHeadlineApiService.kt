package com.example.data.remote.apiservices

import com.example.domain.model.news.NewsHeadLineResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsHeadlineApiService {
    @GET("top-headlines")
    suspend fun getNewsHeadline(
        @Query("country") country:String,
        @Query("apiKey") apiKey:String="9e22269a99384e3f959ac2204791f261"
    ):NewsHeadLineResponse
}