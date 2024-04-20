package com.example.data.di//package com.example.newproject.di

import com.example.data.remote.apiservices.NewsHeadlineApiService
import com.example.data.util.DataConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
//    @Provides
//    @Singleton
//    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//        OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .build()
//    } else OkHttpClient
//        .Builder()
//        .build()


    @Singleton
    @Provides
    fun providesRetrofitNewsHeadline(): NewsHeadlineApiService {
        return Retrofit.Builder()
            .baseUrl(DataConstants.NEWS_HEADLINE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(NewsHeadlineApiService::class.java)
    }
}