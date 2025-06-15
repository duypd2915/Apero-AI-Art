package com.apero.aperoaiart.utils

import android.app.Activity
import android.graphics.Color
import android.net.Uri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.SavedStateHandle

inline fun <reified T> SavedStateHandle.requireArg(key: String): T {
    return this[key] ?: error("Missing argument for key: $key")
}

inline fun <reified T> SavedStateHandle.getArgOrNull(key: String): T? {
    return this[key]
}

fun Uri?.isNotNullOrEmpty(): Boolean {
    return this != null && this != Uri.EMPTY
}

fun Activity.hideNavigationBar() {
    WindowInsetsControllerCompat(window, window.decorView).apply {
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        hide(WindowInsetsCompat.Type.navigationBars())
    }
}

fun Activity.transparentStatusBar() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = Color.WHITE
    WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
}

