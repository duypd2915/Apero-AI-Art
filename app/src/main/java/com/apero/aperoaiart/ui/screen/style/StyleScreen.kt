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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.apero.aperoaiart.R
import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.data.CategoryModel
import com.apero.aperoaiart.data.StyleModel
import com.apero.aperoaiart.ui.components.BottomButton
import com.apero.aperoaiart.ui.components.ShimmerBox
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
    viewModel: StyleViewModel = koinViewModel(),
    onGenerateSuccess: (resultUrl: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val activity = LocalActivity.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.updateCurrentImage(uri)
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            imageLauncher.launch("image/*")
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

    StyleScreenContent(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        uiState = uiState,
        onPromptChange = { viewModel.updatePrompt(it) },
        isImageValid = viewModel.isCurrentImageValid(),
        onOpenPickerWithCheckPermission = {
            if (!permissionUtil.hasStoragePermission()) {
                cameraPermissionLauncher.launch(permissionUtil.getStoragePermission())
            } else {
                imageLauncher.launch("image/*")
            }
        },
        onCategoryClick = { viewModel.updateTabIndex(it) },
        onStyleClick = { viewModel.updateSelectedStyle(it) },
        onGenerateClick = {
            viewModel.generateImage(context = context, onSuccess = onGenerateSuccess)
        }
    )
}

@Composable
private fun StyleScreenContent(
    modifier: Modifier,
    uiState: StyleUiState,
    isImageValid: Boolean,
    onOpenPickerWithCheckPermission: () -> Unit,
    onPromptChange: (String) -> Unit,
    onCategoryClick: (Int) -> Unit,
    onStyleClick: (StyleModel) -> Unit,
    onGenerateClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppColor.Background)
            .padding(horizontal = 23.pxToDp())
    ) {
        if (uiState.generatingState is BaseUIState.Loading) {
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
            PromptInput(
                prompt = uiState.prompt,
                onPromptChange = { onPromptChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
            )

            ImagePickerView(
                isImageValid = isImageValid,
                imageUri = uiState.imageUrl,
                onOpenPickerWithCheckPermission = onOpenPickerWithCheckPermission
            )

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
                when (val categories = uiState.categories) {
                    is BaseUIState.Success ->
                        CategoryTabLoaded(
                            tabIndex = uiState.tabIndex,
                            categories = categories.data,
                            onCategoryClick = onCategoryClick,
                            onStyleClick = onStyleClick,
                            selectedStyle = uiState.selectedStyle
                        )


                    is BaseUIState.Loading,
                    is BaseUIState.Error -> {
                        CategoryTabLoading()
                    }

                    is BaseUIState.Idle -> {}
                }
            }
        }

        BottomButton(
            isEnabled = isImageValid,
            modifier = Modifier
                .align(Alignment.BottomCenter),
            onClick = { onGenerateClick() }
        )
    }
}

@Composable
private fun ImagePickerView(
    isImageValid: Boolean,
    imageUri: Uri?,
    onOpenPickerWithCheckPermission: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(
                shape = RoundedCornerShape(16.pxToDp()),
                color = AppColor.Primary,
                width = 2.pxToDp()
            ),
    ) {
        if (isImageValid) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = imageUri,
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
                        .clickable { onOpenPickerWithCheckPermission() }
                )
            }

        } else {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable { onOpenPickerWithCheckPermission() },
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
}

@Composable
private fun PromptInput(
    prompt: String,
    onPromptChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = prompt,
        onValueChange = { onPromptChange(it) },
        shape = RoundedCornerShape(16.pxToDp()),
        label = {
            Text(
                text = stringResource(R.string.enter_prompt),
                color = AppColor.TextSecondary
            )
        },
        textStyle = AppTypography.StylePromptInput,
        modifier = modifier
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
}

@Composable
private fun CategoryTabLoaded(
    modifier: Modifier = Modifier,
    tabIndex: Int,
    selectedStyle: StyleModel?,
    categories: List<CategoryModel>,
    onCategoryClick: (Int) -> Unit,
    onStyleClick: (StyleModel) -> Unit
) {
    ScrollableTabRow(
        modifier = modifier.fillMaxWidth(),
        selectedTabIndex = tabIndex,
        containerColor = AppColor.Background,
        contentColor = AppColor.Primary,
        edgePadding = 0.pxToDp(),
        divider = {},
        indicator = { tabPositions ->
            val currentTab = tabPositions[tabIndex]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.BottomStart)
                    .offset(x = currentTab.left + (currentTab.width - 16.pxToDp()) / 2)
                    .width(16.pxToDp())
                    .height(2.pxToDp())
                    .background(AppColor.Primary, shape = CircleShape)
            )
        }
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = tabIndex == index,
                onClick = { onCategoryClick(index) },
                modifier = Modifier.clip(CircleShape)
            ) {
                Text(
                    text = category.name,
                    style = AppTypography.StyleCategory,
                    color = if (tabIndex == index) AppColor.Primary else AppColor.TextPrimary,
                    modifier = Modifier.padding(
                        horizontal = 12.pxToDp(),
                        vertical = 4.pxToDp()
                    )
                )
            }
        }
    }

    LazyRow {
        items(categories[tabIndex].styles) { style ->
            StyleItem(
                model = style,
                onStyleClick = onStyleClick,
                isSelected = selectedStyle == style,
            )
        }
    }
}

@Composable
private fun StyleItem(
    model: StyleModel,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onStyleClick: (StyleModel) -> Unit,
) {
    val borderModifier = if (isSelected) {
        Modifier.border(
            width = 2.pxToDp(),
            color = AppColor.TextBlue,
            shape = RoundedCornerShape(12.pxToDp())
        )
    } else {
        Modifier
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(end = 11.pxToDp())
            .clickable { onStyleClick(model) }
            .clip(RoundedCornerShape(12.pxToDp()))
    ) {
        AsyncImage(
            model = model.image,
            contentDescription = model.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.pxToDp())
                .clip(RoundedCornerShape(12.pxToDp()))
                .then(borderModifier)
        )
        Text(
            text = model.name,
            color = if (isSelected) AppColor.TextBlue else AppColor.TextPrimary,
            style = AppTypography.StyleChooseItem,
            modifier = Modifier.padding(top = 8.pxToDp())
        )
    }
}

@Composable
private fun CategoryTabLoading() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.pxToDp()),
    ) {
        items(5) {
            Text("___", color = AppColor.TextSecondary)
        }
    }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.pxToDp()),
    ) {
        items(5) {
            ShimmerBox(modifier = Modifier.size(80.pxToDp()))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun StyleScreenPreview() {
    StyleScreenContent(
        modifier = Modifier,
        uiState = StyleUiState(),
        isImageValid = true,
        onOpenPickerWithCheckPermission = { },
        onPromptChange = { },
        onCategoryClick = { },
        onStyleClick = { },
        onGenerateClick = { }
    )
}

@Preview(showBackground = true)
@Composable
fun CategoryTabLoadingPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.pxToDp(), horizontal = 23.pxToDp()),
        verticalArrangement = Arrangement.spacedBy(15.pxToDp())
    ) {
        CategoryTabLoading()
    }
}

@Preview(showBackground = true)
@Composable
fun ImagePickerPreview() {
    ImagePickerView(
        modifier = Modifier,
        isImageValid = false,
        onOpenPickerWithCheckPermission = { },
        imageUri = null
    )
}

