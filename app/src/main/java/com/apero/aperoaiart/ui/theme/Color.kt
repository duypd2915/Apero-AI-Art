package com.apero.aperoaiart.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppColor {
    val Primary = Color(0xFFE400D9)
    val Secondary = Color(0xFFCCC2DC)
    val Background = Color(0xFFFFFFFF)
    val BackgroundLoading = Color(0xFFF5F5F5)
    val ButtonGradient = Brush.horizontalGradient(
        listOf(Color(0xFFE400D9), Color(0xFF1D00F5)),
    )
    val Transparent = Color(0x00000000)
    val AppBlue = Color(0xFF1D00F5)

    val TextPrimary = Color(0xFF000000)
    val TextSecondary = Color(0xFFAAAAAA)
    val TextWhite = Color(0xFFFFFFFF)
    val TextBlue = Color(0xFF1D00F5).copy(alpha = 0.9f)
}