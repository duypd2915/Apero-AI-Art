package com.apero.aperoaiart.ui.screen.pickphoto

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.apero.aperoaiart.R
import com.apero.aperoaiart.ui.theme.AppColor
import com.apero.aperoaiart.ui.theme.AppTypography
import com.apero.aperoaiart.ui.theme.pxToDp
import com.duyhellowolrd.ai_art_service.data.PhotoItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun PickPhotoScreen(
    modifier: Modifier = Modifier,
    viewModel: PickPhotoViewModel = koinViewModel(),
    onBack: () -> Unit,
    onNext: (selectedUri: String) -> Unit
) {
    val selectedPhoto by viewModel.selectedPhoto.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val imageSizePx =
        remember(configuration.screenWidthDp) { (configuration.screenWidthDp - 60) / 3 }
    val lazyPagingItems: LazyPagingItems<PhotoItem> =
        viewModel.photoPagingFlow.collectAsLazyPagingItems()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppColor.Background)
            .padding(top = 23.pxToDp())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.pxToDp())
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "Close",
                modifier = Modifier
                    .size(40.pxToDp())
                    .clickable { onBack() }
                    .padding(start = 16.pxToDp())
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Next",
                style = AppTypography.NextPickPhoto,
                color = AppColor.TextPrimary,
                modifier = Modifier
                    .clickable(enabled = selectedPhoto != null) {
                        onNext(selectedPhoto?.uri.toString())
                    }
                    .padding(end = 16.pxToDp())
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.pxToDp()),
            contentPadding = PaddingValues(horizontal = 10.pxToDp(), vertical = 6.pxToDp())
        ) {
            items(lazyPagingItems.itemCount) { index ->
                val item = lazyPagingItems[index]
                val isSelected = item?.id == selectedPhoto?.id
                Box(
                    modifier = Modifier
                        .padding(5.pxToDp())
                        .size(imageSizePx.pxToDp())
                        .clip(RoundedCornerShape(8.pxToDp()))
                        .border(
                            width = 1.pxToDp(),
                            color = AppColor.Transparent,
                            shape = RoundedCornerShape(8.pxToDp())
                        )
                        .clickable { viewModel.selectPhoto(item) }
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item?.uri)
                            .size(imageSizePx)
                            .crossfade(true)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.pxToDp())
                                .size(24.pxToDp())
                                .background(
                                    AppColor.Primary,
                                    shape = RoundedCornerShape(12.pxToDp())
                                )
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = "Selected",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}