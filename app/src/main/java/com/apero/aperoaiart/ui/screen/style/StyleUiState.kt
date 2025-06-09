package com.apero.aperoaiart.ui.screen.style

import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.data.StyleModel

data class StyleUiState(
    // TODO
    val genArtState: BaseUIState<StyleModel> = BaseUIState.Idle,
    val styleList: BaseUIState<List<StyleModel>> = BaseUIState.Idle,
    val prompt: String = "",
    val imageUrl: String = ""
)