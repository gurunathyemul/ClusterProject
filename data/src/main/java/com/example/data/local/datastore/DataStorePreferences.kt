package com.example.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.util.DataConstants.DATA
import com.example.data.util.DataConstants.DATA_STORE_PREF_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStorePreferences @Inject constructor(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        DATA_STORE_PREF_NAME
    )


    private val data = stringPreferencesKey(DATA)
    suspend fun saveData(value: String) {
        context.dataStore.edit { mutablePreferences -> mutablePreferences[data] = value }
    }

    fun getData(): Flow<String> =
        context.dataStore.data.map { value: Preferences -> value[data] ?: "data null" }
}