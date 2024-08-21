package com.example.clusterproject.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings

object PermissionUtils {
    val LocationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    //  Navigate to settings screen on permission denied
    fun navigateToAppPermissionSettings(context: Context) {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }

    fun haveLocationPermissions(context: Context): Boolean =
        LocationPermissions.all { context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }
}