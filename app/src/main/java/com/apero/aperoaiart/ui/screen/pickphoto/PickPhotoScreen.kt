package com.apero.aperoaiart.ui.screen.pickphoto

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.apero.aperoaiart.R
import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.ui.theme.AppColor
import com.apero.aperoaiart.ui.theme.AppTypography
import com.apero.aperoaiart.ui.theme.pxToDp
import com.apero.aperoaiart.utils.UiConstant
import com.apero.aperoaiart.utils.singleClickable
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PickPhotoScreen(
    modifier: Modifier = Modifier,
    viewModel: PickPhotoViewModel = koinViewModel(),
    onBack: () -> Unit,
    onNext: (selectedUri: String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val photos = when (val state = uiState.photoState) {
        is BaseUIState.Success -> state.data
        else -> emptyList()
    }
    val gridState = rememberLazyGridState()

//    LaunchedEffect(gridState, photos) {
//        snapshotFlow {
//            val lastVisible = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
//            lastVisible >= photos.size - 3
//        }.collect { shouldLoadMore ->
//            if (shouldLoadMore) {
//                viewModel.loadNextPage()
//            }
//        }
//    }
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
                    .singleClickable { onBack() }
                    .padding(start = 16.pxToDp())
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Next",
                style = AppTypography.NextPickPhoto,
                color = AppColor.TextPrimary,
                modifier = Modifier
                    .singleClickable {
                        uiState.selectedPhoto?.url?.let { onNext(it) }
                    }
                    .padding(end = 16.pxToDp())
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            state = gridState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.pxToDp()),
            contentPadding = PaddingValues(horizontal = 10.pxToDp(), vertical = 6.pxToDp())
        ) {
            items(photos.size) { index ->
                val item = photos[index]
                val isSelected = viewModel.isSelected(index)
                Box(
                    modifier = Modifier
                        .padding(5.pxToDp())
                        .size(UiConstant.preferImageSize.pxToDp())
                        .clip(RoundedCornerShape(8.pxToDp()))
                        .border(
                            width = 1.pxToDp(),
                            color = if (isSelected) AppColor.Primary else AppColor.Transparent,
                            shape = RoundedCornerShape(8.pxToDp())
                        )
                        .singleClickable { viewModel.selectPhoto(item) }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(item.url),
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