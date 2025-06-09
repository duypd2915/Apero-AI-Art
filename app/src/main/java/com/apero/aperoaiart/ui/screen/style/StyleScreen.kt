package com.apero.aperoaiart.ui.screen.style

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.apero.aperoaiart.R
import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.data.StyleModel
import com.apero.aperoaiart.ui.components.BottomButton
import com.apero.aperoaiart.ui.theme.AppColor
import com.apero.aperoaiart.ui.theme.AppTypography
import com.apero.aperoaiart.ui.theme.pxToDp
import com.apero.aperoaiart.utils.PermissionUtil
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun StyleScreen(
    modifier: Modifier = Modifier,
    permissionUtil: PermissionUtil = koinInject(),
    viewModel: StyleViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val activity = LocalActivity.current
    val focusManager = LocalFocusManager.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.updateCurrentImage(uri)
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launcher.launch("image/*")
        } else {
            if (activity == null) return@rememberLauncherForActivityResult
            if (!permissionUtil.canShowStorageRational(activity)) {
                permissionUtil.openAppSettings(activity)
            }
        }
    }
    BackHandler {
        focusManager.clearFocus()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppColor.Background)
            .padding(horizontal = 23.pxToDp())
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        if (uiState.selectedStyle is BaseUIState.Loading) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(AppColor.BackgroundLoading)
            ) {
                // TODO: wait lottie
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 27.pxToDp(), bottom = 72.pxToDp()), // Leave space for button
            verticalArrangement = Arrangement.spacedBy(27.pxToDp())
        ) {
            TextField(
                value = uiState.prompt,
                onValueChange = { viewModel.updatePrompt(it) },
                shape = RoundedCornerShape(16.pxToDp()),
                label = {
                    Text(
                        text = stringResource(R.string.enter_prompt),
                        color = AppColor.TextSecondary
                    )
                },
                textStyle = AppTypography.StylePromptInput,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.pxToDp())
                    .border(
                        shape = RoundedCornerShape(16.pxToDp()),
                        color = AppColor.Primary,
                        width = 2.pxToDp(),
                    ),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = AppColor.Background,
                    focusedContainerColor = AppColor.Background,
                    disabledContainerColor = AppColor.Background,
                    cursorColor = AppColor.Primary,
                    focusedIndicatorColor = AppColor.Transparent,
                    unfocusedIndicatorColor = AppColor.Transparent
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .border(
                        shape = RoundedCornerShape(16.pxToDp()),
                        color = AppColor.Primary,
                        width = 2.pxToDp()
                    ),
            ) {
                if (viewModel.isCurrentImageValid()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = uiState.imageUrl,
                            contentDescription = "",
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier
                                .align(Alignment.Center),
                        )
                        Image(
                            painter = painterResource(id = R.drawable.change_photo),
                            contentDescription = "",
                            alignment = Alignment.TopStart,
                            modifier = Modifier
                                .padding(top = 18.pxToDp(), start = 23.pxToDp())
                                .clickable {
                                    launcher.launch("image/*")
                                }
                        )
                    }

                } else {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clickable {
                                if (!permissionUtil.hasStoragePermission()) {
                                    cameraLauncher.launch(permissionUtil.getStoragePermission())
                                } else {
                                    launcher.launch("image/*")
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.pxToDp())
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_add_photot),
                            contentDescription = ""
                        )
                        Text(
                            text = stringResource(R.string.add_your_photo),
                            color = AppColor.TextSecondary,
                            style = AppTypography.StyleAddPhoto,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(15.pxToDp())
            ) {
                Text(
                    text = stringResource(R.string.choose_style),
                    color = AppColor.Primary,
                    style = AppTypography.StyleChooseItem
                )
                when (val styleList = uiState.styleList) {
                    is BaseUIState.Success -> {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(11.pxToDp())
                        ) {
                            items(styleList.data) {
                                StyleItem(
                                    model = it
                                )
                            }
                        }
                    }

                    is BaseUIState.Loading -> {}
                    is BaseUIState.Error -> {}
                    is BaseUIState.Idle -> {}
                }
            }
        }

        BottomButton(
            isEnabled = viewModel.isCurrentImageValid(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}


@Composable
fun StyleItem(
    model: StyleModel,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        AsyncImage(
            model = model.image,
            contentDescription = model.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.pxToDp())
                .clip(RoundedCornerShape(12.pxToDp()))
        )
        Text(
            text = model.name,
            color = AppColor.TextPrimary,
            style = AppTypography.StyleChooseItem,
            modifier = Modifier.padding(top = 8.pxToDp())
        )
    }
}


@Preview(showBackground = true)
@Composable
fun StyleScreenPreview() {
    StyleScreen()
}
