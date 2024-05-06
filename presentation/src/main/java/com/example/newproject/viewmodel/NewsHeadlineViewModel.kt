package com.example.newproject.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.domain.base.UseCaseCallback
import com.example.domain.exceptions.ErrorModel
import com.example.domain.model.news.Article
import com.example.domain.model.news.NewsHeadLineResponse
import com.example.domain.usecase.NewsHeadLineUseCase
import com.example.newproject.R
import com.example.newproject.adapter.NewsHeadlineAdapter
import com.example.newproject.base.BaseAndroidViewModel
import com.example.newproject.other.Resource
import com.example.newproject.ui.ContentInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsHeadlineViewModel @Inject constructor(
    context: Application,
    private val newsHeadLineUseCase: NewsHeadLineUseCase
) :
    BaseAndroidViewModel(context) {
    private var contentInterface: ContentInterface? = null
    private val _newsHeadlines: MutableStateFlow<Resource<NewsHeadLineResponse>> =
        MutableStateFlow(Resource.Loading())

    // Expose the StateFlow to observe changes
    val newsHeadlines: StateFlow<Resource<NewsHeadLineResponse>>
        get() = _newsHeadlines

    var newsHeadlineAdapter: NewsHeadlineAdapter? = null
        private set

    init {
        newsHeadlineAdapter = NewsHeadlineAdapter(R.layout.rv_item_news, this)
    }

    fun getNewsHeadline(country: String) {
        _newsHeadlines.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            newsHeadLineUseCase.call(country, object : UseCaseCallback<NewsHeadLineResponse> {
                override fun onSuccess(result: NewsHeadLineResponse) {
                    Log.d(TAG, "onSuccess: $result")
                    _newsHeadlines.value = Resource.Success(result)
                }

                override fun onError(errorModel: ErrorModel?) {
                    Log.d(TAG, "onError:: ${errorModel?.message}(${errorModel?.errorStatus})")
                    _newsHeadlines.value = Resource.Error(errorModel?.message!!)
                }
            })
        }
    }

    fun setNewsHeadlineData(data: List<Article?>?, contentInterface: ContentInterface) {
        this.contentInterface = contentInterface
        newsHeadlineAdapter?.setData(data)
    }

    fun newsItemClick(data: String?) {
        contentInterface?.onClick(data)
    }

    fun clearData() {
        newsHeadlineAdapter = null
        contentInterface = null
        _newsHeadlines.value = Resource.Loading()
    }

    companion object {
        private const val TAG = "NewsDetailsViewModel"
    }
}