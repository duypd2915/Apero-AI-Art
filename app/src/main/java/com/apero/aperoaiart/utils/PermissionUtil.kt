package com.apero.aperoaiart.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat

class PermissionUtil(private val context: Context) {

    fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasReadStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, getReadStoragePermission()
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasWriteStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getReadStoragePermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    fun getWriteStoragePermission(): String {
        return Manifest.permission.WRITE_EXTERNAL_STORAGE
    }

    fun canShowCameraRationale(activity: Activity): Boolean {
        return activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
    }

    fun canShowReadStorageRational(activity: Activity): Boolean {
        return activity.shouldShowRequestPermissionRationale(getReadStoragePermission())
    }

    fun canShowWriteStorageRational(activity: Activity): Boolean {
        return activity.shouldShowRequestPermissionRationale(getWriteStoragePermission())
    }

    fun openAppSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        activity.startActivity(intent)
    }
}
