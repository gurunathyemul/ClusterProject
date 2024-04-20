package com.example.newproject.adapter

import com.example.domain.model.news.Article
import com.example.newproject.base.BaseAdapter
import com.example.newproject.base.BaseAndroidViewModel

class NewsHeadlineAdapter(private val layoutId: Int, baseAndroidViewModel: BaseAndroidViewModel) :
    BaseAdapter(baseAndroidViewModel) {
    private var data: List<Article?>? = null
    fun setData(data: List<Article?>?) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getObjForPosition(position: Int) = data?.get(position) ?: 0

    override fun getLayoutIdForPosition(position: Int) = layoutId

    override fun getItemCount(): Int = data?.size ?: 0
}