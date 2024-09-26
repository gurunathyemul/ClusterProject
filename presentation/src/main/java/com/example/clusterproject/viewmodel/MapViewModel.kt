package com.example.clusterproject.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.clusterproject.R
import com.example.clusterproject.adapter.SuggestedLocationAdapter
import com.example.clusterproject.base.BaseAndroidViewModel
import com.mappls.sdk.services.api.OnResponseCallback
import com.mappls.sdk.services.api.autosuggest.MapplsAutoSuggest
import com.mappls.sdk.services.api.autosuggest.MapplsAutosuggestManager
import com.mappls.sdk.services.api.autosuggest.model.AutoSuggestAtlasResponse
import com.mappls.sdk.services.api.autosuggest.model.ELocation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(context: Application) :
    BaseAndroidViewModel(context) {
    private val _selectedLocation = MutableLiveData<ELocation?>()
    val observeSelectedLocation: MutableLiveData<ELocation?> = _selectedLocation
    var adapter = SuggestedLocationAdapter(R.layout.rv_item_suggested_locations, this)
        private set

    fun setData(autoSuggestedLocList: ArrayList<ELocation>?) {
        adapter.setData(autoSuggestedLocList)
        adapter.notifyDataSetChanged()
    }

    fun onLocationItemClick(obj: ELocation?) {
        Log.d(TAG, "onLocationItemClick: $obj")
        _selectedLocation.postValue(obj)
    }

    fun getAutoSuggestLocList(query: String) {
        MapplsAutoSuggest.builder().query(query).build().also {
            MapplsAutosuggestManager.newInstance(it)
                .call(object : OnResponseCallback<AutoSuggestAtlasResponse> {
                    override fun onSuccess(response: AutoSuggestAtlasResponse?) {
                        val locationList: ArrayList<ELocation>? =
                            response?.suggestedLocations
                        Log.d(TAG, "onSuccess::$locationList")
                        setData(locationList)
                    }

                    override fun onError(p0: Int, p1: String?) {
                        Log.d(TAG, "onError: $p1")
                    }
                })
        }
    }

    companion object {
        private const val TAG = "MapViewModel"
    }
}