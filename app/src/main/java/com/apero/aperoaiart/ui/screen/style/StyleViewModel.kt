package com.apero.aperoaiart.ui.screen.style

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.base.BaseViewModel
import com.apero.aperoaiart.data.toModel
import com.apero.aperoaiart.navigation.SaveStateConstants
import com.duyhellowolrd.ai_art_service.data.AiArtRepository
import com.duyhellowolrd.ai_art_service.network.response.StyleData
import kotlinx.coroutines.launch

class StyleViewModel(
    private val aiArtRepository: AiArtRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<StyleUiState>(StyleUiState()) {

    init {
        viewModelScope.launch {
            val responseApi = aiArtRepository.getAllStyles()
            updateState { state ->
                state.copy(
                    styleList = BaseUIState.Success(
                        responseApi.getOrDefault(StyleData()).items
                            .map { it.toModel() })
                )
            }
        }
    }

    fun generateImage() {
        updateState {
            it.copy(genArtState = BaseUIState.Loading)
        }
        viewModelScope.launch {
            savedStateHandle[SaveStateConstants.KEY_IMAGE_URI] = uiState.value.imageUrl
        }
    }

}



