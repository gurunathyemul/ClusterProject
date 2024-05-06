package com.example.newproject.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat

object PermissionUtils {
    val CONTACT_PERMISSION = arrayOf(
        Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS
    )

    fun grantedContactPermission(context: Context): Boolean =
        CONTACT_PERMISSION.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

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
}