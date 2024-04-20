package com.example.data.local.datastore

import android.content.Context
import android.content.SharedPreferences
import com.example.data.util.DataConstants.DATA
import com.example.data.util.DataConstants.SHARED_PREFERENCE_NAME
import javax.inject.Inject

class SharedDataPreferences @Inject constructor(private val context: Context) {

    //todo check with adding edit()
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun saveData(data: String) {
        sharedPreferences.edit().putString(DATA, data).apply()
    }

    fun getData() = sharedPreferences.getString(DATA, "data null")
}