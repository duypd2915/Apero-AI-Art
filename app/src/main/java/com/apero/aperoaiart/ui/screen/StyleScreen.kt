package com.apero.aperoaiart.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import coil.compose.AsyncImage
import com.apero.aperoaiart.R
import com.apero.aperoaiart.ui.theme.AppColor
import com.apero.aperoaiart.ui.theme.pxToDp

@Composable
fun StyleScreen(
    modifier: Modifier = Modifier,
) {
    var editTextValue by remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppColor.Background)
            .padding(horizontal = 23.pxToDp())
    ) {
        TextField(
            value = editTextValue,
            onValueChange = { editTextValue = it },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .border(color = AppColor.Primary, width = 2.pxToDp())
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f)
                .border(color = AppColor.Primary, width = 2.pxToDp())
        ) {
            AsyncImage(
                contentDescription = "",
                imageLoader = ImageLoader.Builder(context).build(),
                model = "https://www.shutterstock.com/image-photo/vietnamese-girl-native-dress-260nw-1344965396.jpg",
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.pxToDp())
        ) {
            Text(
                text = stringResource(R.string.choose_style),
                color = AppColor.Primary
            )
        }

        Text(
            text = stringResource(R.string.btn_generate_art)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StyleScreenPreview() {
    StyleScreen()
}
