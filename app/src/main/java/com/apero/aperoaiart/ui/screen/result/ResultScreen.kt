package com.apero.aperoaiart.ui.screen.result

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.apero.aperoaiart.R
import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.ui.components.BottomButton
import com.apero.aperoaiart.ui.components.LoadingFullScreen
import com.apero.aperoaiart.ui.theme.AppColor
import com.apero.aperoaiart.ui.theme.pxToDp
import com.apero.aperoaiart.utils.PermissionUtil
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    viewModel: ResultViewModel = koinViewModel(),
    permissionUtil: PermissionUtil = koinInject(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val activity = LocalActivity.current
    val writeExPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onDownloadClick()
        } else {
            if (activity == null) return@rememberLauncherForActivityResult
            if (!permissionUtil.canShowWriteStorageRational(activity)) {
                permissionUtil.openAppSettings(activity)
            }
        }
    }

    BackHandler {
        onBack()
    }

    ResultScreenContent(
        uiState = uiState,
        modifier = modifier,
        onBack = onBack,
        onDownloadClick = {
//            if (!permissionUtil.hasWriteStoragePermission()) {
//                writeExPermissionLauncher.launch(permissionUtil.getWriteStoragePermission())
//            } else {
            viewModel.onDownloadClick()
//            }
        }
    )
}

@Composable
private fun ResultScreenContent(
    uiState: ResultUiState,
    modifier: Modifier = Modifier,
    onDownloadClick: () -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppColor.Background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 11.pxToDp(), top = 25.pxToDp()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(40.pxToDp())
                    .clickable(enabled = uiState.downloadState !is BaseUIState.Loading) { onBack() }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 23.pxToDp()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.pxToDp()))
                    .border(
                        shape = RoundedCornerShape(16.pxToDp()),
                        color = AppColor.Primary,
                        width = 2.pxToDp()
                    )
            ) {
                AsyncImage(
                    model = uiState.imageUrl,
                    contentDescription = "",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            BottomButton(
                isEnabled = uiState.downloadState !is BaseUIState.Loading,
                text = R.string.btn_download,
                onClick = onDownloadClick,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        LoadingFullScreen(
            isVisible = uiState.downloadState is BaseUIState.Loading,
            title = R.string.text_downloading
        )
    }
}