package com.example.clusterproject.base

import android.app.Application
import android.util.Log
import com.mappls.sdk.maps.Mappls
import com.mappls.sdk.services.account.MapplsAccountManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: BaseApplication")
        MapplsAccountManager.getInstance().restAPIKey = "4f7c0c619ed630026756c4969cc30da7"
        MapplsAccountManager.getInstance().mapSDKKey = "4f7c0c619ed630026756c4969cc30da7"
        MapplsAccountManager.getInstance().atlasClientId =
            "96dHZVzsAuvwMa-cOHGFsMo9pQ4V2aWrVc9qQdRwH082BSACX7UVeCm0jVGCjEYJ3odQVPpMY_YDBiCQtqXh-g=="
        MapplsAccountManager.getInstance().atlasClientSecret =
            "lrFxI-iSEg_9H1KFVKIoDigqJJZjxdwkNL9boO_y_aupsAJkb20aaaev6xN9NnMCxhGJKBBpiJwI9l33Ur8xZtLUxgHdQS-Q"
        Mappls.getInstance(this)
    }

    companion object {
        private const val TAG = "BaseApplication"
    }
}