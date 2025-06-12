package com.apero.aperoaiart.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.apero.aperoaiart.R

object AppTypography {
    val StylePromptInput = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )

    val StyleChooseItem = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
        lineHeight = 21.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Normal
    )

    val StyleAddPhoto = TextStyle(
        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    )

    val StyleCategory = TextStyle(
        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
    )

    val DialogLoading = TextStyle(
        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    )

    val SnackBarText = TextStyle(
        fontFamily = FontFamily(Font(R.font.plus_jakarta_sans)),
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 21.sp,
    )
}