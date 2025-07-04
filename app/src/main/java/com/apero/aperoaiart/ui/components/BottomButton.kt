package com.apero.aperoaiart.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.apero.aperoaiart.R
import com.apero.aperoaiart.ui.theme.AppColor
import com.apero.aperoaiart.ui.theme.AppTypography
import com.apero.aperoaiart.ui.theme.pxToDp

@Composable
fun BottomButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    @StringRes text: Int = R.string.btn_generate_art,
    onClick: () -> Unit
) {
    Text(
        text = stringResource(text),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 72.pxToDp())
            .clickable { onClick() }
            .background(
                alpha = if (isEnabled) 1f else 0.5f,
                brush = AppColor.ButtonGradient,
                shape = RoundedCornerShape(12.pxToDp())
            )
            .padding(vertical = 16.pxToDp())
            .padding(horizontal = 8.pxToDp()),
        color = AppColor.TextWhite,
        textAlign = TextAlign.Center,
        style = AppTypography.StyleChooseItem,
    )
}