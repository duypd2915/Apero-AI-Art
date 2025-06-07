package com.apero.aperoaiart.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val LocalScreenScale = compositionLocalOf { 1f }
var cachedDensityScale: Float = 1f

@Composable
fun Number.pxToDpCompose(): Dp {
    val scale = LocalScreenScale.current
    return (this.toFloat() * scale).dp
}

fun Number.pxToDp(): Dp {
    val scale = cachedDensityScale
    return (this.toFloat() * scale).dp
}

fun Number.pxToSp(): TextUnit {
    val scale = cachedDensityScale
    return (this.toFloat() * scale).sp
}
