package com.apero.aperoaiart.ui.screen.style

import android.net.Uri
import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.data.CategoryModel
import com.apero.aperoaiart.data.StyleModel

data class StyleUiState(
    val selectedStyle: BaseUIState<StyleModel> = BaseUIState.Idle,
    val styleList: BaseUIState<List<CategoryModel>> = BaseUIState.Idle,
    val prompt: String = "",
    val imageUrl: Uri? = null
)