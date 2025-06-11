package com.apero.aperoaiart.utils

import androidx.lifecycle.SavedStateHandle

inline fun <reified T> SavedStateHandle.requireArg(key: String): T {
    return this[key] ?: error("Missing argument for key: $key")
}
