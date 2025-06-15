package com.apero.aperoaiart.utils

import android.app.Activity
import android.graphics.Color
import android.net.Uri
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

fun Modifier.singleClickable(
    enabled: Boolean = true,
    delayMillis: Long = 500L,
    indication: Indication? = null,
    onClick: () -> Unit
): Modifier = composed {
    var isClicking by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    this.clickable(
        enabled = enabled && !isClicking,
        interactionSource = interactionSource,
        indication = indication
    ) {
        if (!isClicking) {
            isClicking = true
            onClick()
            scope.launch {
                delay(delayMillis)
                isClicking = false
            }
        }
    }
}


