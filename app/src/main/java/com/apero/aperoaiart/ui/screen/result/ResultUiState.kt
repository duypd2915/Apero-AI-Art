package com.apero.aperoaiart.ui.screen.result

import com.apero.aperoaiart.base.BaseUIState

data class ResultUiState(
    val imageUrl: String = "",
    val downloadState: BaseUIState<Unit> = BaseUIState.Idle
)
