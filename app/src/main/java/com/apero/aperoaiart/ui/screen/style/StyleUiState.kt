package com.apero.aperoaiart.ui.screen.style

import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.data.ResultModel

data class StyleUiState(
    val state: BaseUIState<ResultModel> = BaseUIState.Idle
)