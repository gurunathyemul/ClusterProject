package com.example.clusterproject.adapter

import com.example.clusterproject.base.BaseAdapter
import com.example.clusterproject.base.BaseAndroidViewModel
import com.mappls.sdk.services.api.autosuggest.model.ELocation

class SuggestedLocationAdapter(
    private val layoutId: Int,
    mViewModel: BaseAndroidViewModel,
) : BaseAdapter(mViewModel) {
    private var autoSuggestedLocList: ArrayList<ELocation>? = null

    fun setData(autoSuggestedLocList: ArrayList<ELocation>?) {
        this.autoSuggestedLocList = autoSuggestedLocList
    }

    override fun getItemCount(): Int = autoSuggestedLocList?.size ?: 0

    override fun getObjForPosition(position: Int) = autoSuggestedLocList?.get(position) ?: ""

    override fun getLayoutIdForPosition(position: Int): Int = layoutId
}
