package com.example.domain.usecase

import com.example.domain.base.UseCase
import com.example.domain.exceptions.IErrorHandler
import com.example.domain.model.news.NewsHeadLineResponse
import com.example.domain.repositories.NewsHeadLineRepository
import javax.inject.Inject

class NewsHeadLineUseCase @Inject constructor(
    private val newsHeadLineRepository: NewsHeadLineRepository,
    errorHandler: IErrorHandler
) : UseCase<NewsHeadLineResponse, String>(errorHandler) {

    override suspend fun run(params: String?): NewsHeadLineResponse =
        newsHeadLineRepository.getNewsHeadline(params!!)
}