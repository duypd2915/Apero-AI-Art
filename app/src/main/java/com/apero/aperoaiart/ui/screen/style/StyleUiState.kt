package com.apero.aperoaiart.ui.screen.style

import android.net.Uri
import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.data.CategoryModel
import com.apero.aperoaiart.data.StyleModel

data class StyleUiState(
    val selectedStyle: StyleModel? = null,
    val categories: BaseUIState<List<CategoryModel>> = BaseUIState.Idle,
    val prompt: String = "",
    val imageUrl: Uri? = null,
    val tabIndex: Int = 0,
    val generatingState: BaseUIState<Unit> = BaseUIState.Idle // todo replace Unit
)