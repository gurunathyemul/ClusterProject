package com.example.newproject.viewmodel

import android.app.Application
import com.example.domain.model.news.Article
import com.example.newproject.R
import com.example.newproject.adapter.NewsHeadlineAdapter
import com.example.newproject.base.BaseAndroidViewModel
import com.example.newproject.ui.ContentInterface
import javax.inject.Inject

//@HiltViewModel
class NewsHeadlineViewModel @Inject constructor(private val context: Application) :
    BaseAndroidViewModel(context) {
    private var contentInterface: ContentInterface? = null
    var newsHeadlineAdapter: NewsHeadlineAdapter =
        NewsHeadlineAdapter(R.layout.rv_item_layout, this)
        private set

    fun setNewsHeadlineData(data: List<Article?>?, contentInterface: ContentInterface) {
        this.contentInterface = contentInterface
        newsHeadlineAdapter.setData(data)
    }

    fun newsItemClick(data: String?) {
        contentInterface?.onClick(data)
    }
}