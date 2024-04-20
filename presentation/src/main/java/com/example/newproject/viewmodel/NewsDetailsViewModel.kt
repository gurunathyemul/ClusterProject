package com.example.newproject.viewmodel
//
import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.domain.base.UseCaseCallback
import com.example.domain.exceptions.ErrorModel
import com.example.domain.model.news.NewsHeadLineResponse
import com.example.domain.usecase.NewsHeadLineUseCase
import com.example.newproject.base.BaseAndroidViewModel
import com.example.newproject.other.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//
@HiltViewModel
class NewsDetailsViewModel @Inject constructor(
    context: Application,
    private val newsHeadLineUseCase: NewsHeadLineUseCase
) :
    BaseAndroidViewModel(context) {

    private val _newsHeadlines: MutableStateFlow<Resource<NewsHeadLineResponse>> =
        MutableStateFlow(Resource.Loading())

    // Expose the StateFlow to observe changes
    val newsHeadlines: StateFlow<Resource<NewsHeadLineResponse>>
        get() = _newsHeadlines

    fun getNewsHeadline(country: String) {
        viewModelScope.launch(Dispatchers.IO) {
            newsHeadLineUseCase.call(country, object : UseCaseCallback<NewsHeadLineResponse> {
                override fun onSuccess(result: NewsHeadLineResponse) {
                    _newsHeadlines.value = Resource.Success(result)
                }

                override fun onError(errorModel: ErrorModel?) {
                    _newsHeadlines.value = Resource.Error(errorModel?.message!!)
                }
            })
        }
    }
}