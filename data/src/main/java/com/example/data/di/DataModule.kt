package com.example.data.di

import android.content.Context
import com.example.data.ErrorHandler
import com.example.data.local.datastore.SharedDataPreferences
import com.example.data.remote.apiservices.NewsHeadlineApiService
import com.example.data.remote.datasource.RemoteDataSource
import com.example.data.remote.datasource.RemoteDataSourceImp
import com.example.data.repositories.NewsHeadlineRepositoryImp
import com.example.domain.exceptions.IErrorHandler
import com.example.domain.repositories.NewsHeadLineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext appContext: Context) =
        SharedDataPreferences(appContext)

    @Provides
    @Singleton
    fun provideErrorHandler(): IErrorHandler = ErrorHandler()

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        newsHeadlineApiService: NewsHeadlineApiService
    ): RemoteDataSource {
        return RemoteDataSourceImp(newsHeadlineApiService)
    }

    @Provides
    @Singleton
    fun provideNewsHeadlineRepository(remoteDataSource: RemoteDataSource): NewsHeadLineRepository =
        NewsHeadlineRepositoryImp(remoteDataSource)
}