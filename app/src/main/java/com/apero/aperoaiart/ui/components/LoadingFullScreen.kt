package com.apero.aperoaiart.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.apero.aperoaiart.R
import com.apero.aperoaiart.ui.theme.AppColor
import com.apero.aperoaiart.ui.theme.AppTypography
import com.apero.aperoaiart.ui.theme.pxToDp

@Composable
fun LoadingFullScreen(
    isVisible: Boolean,
    @StringRes title: Int
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_loading))
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.BackgroundLoading.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .aspectRatio(1.1f)
                    .clip(
                        RoundedCornerShape(
                            topStart = 22.pxToDp(),
                            topEnd = 18.pxToDp(),
                            bottomEnd = 22.pxToDp(),
                            bottomStart = 18.pxToDp()
                        )
                    )
                    .background(AppColor.Background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(width = 120.pxToDp(), height = 100.pxToDp())
                )
                Text(
                    text = stringResource(title),
                    style = AppTypography.DialogLoading,
                    color = AppColor.TextPrimary,
                    modifier = Modifier.padding(bottom = 10.pxToDp())
                )
            }
        }
    }
}

@Composable
@Preview
private fun LoadingFullScreenPreview() {
    LoadingFullScreen(true, R.string.text_downloading)
}