package com.example.clusterproject.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import javax.inject.Inject

open class BaseAndroidViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {
}