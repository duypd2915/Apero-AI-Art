package com.apero.aperoaiart.ui.screen.style

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.base.BaseViewModel
import com.apero.aperoaiart.data.StyleModel
import com.apero.aperoaiart.data.toModel
import com.apero.aperoaiart.navigation.SaveStateConstants
import com.duyhellowolrd.ai_art_service.data.AiArtRepository
import com.duyhellowolrd.ai_art_service.network.consts.ServiceConstants
import kotlinx.coroutines.launch

class StyleViewModel(
    private val aiArtRepository: AiArtRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<StyleUiState>(StyleUiState()) {

    init {
        viewModelScope.launch {
            updateState {
                it.copy(categories = BaseUIState.Loading)
            }
            val responseApi = aiArtRepository.getAllStyles()
            updateState { state ->
                state.copy(
                    categories =
                        responseApi.fold(
                            onSuccess = { data ->
                                BaseUIState.Success(data.items.map { it.toModel() })
                            },
                            onFailure = { exception ->
                                BaseUIState.Error(
                                    exception.message ?: ServiceConstants.UNKNOWN_ERROR_MESSAGE
                                )
                            }
                        )
                )
            }
        }
    }

    fun updateCurrentImage(newUri: Uri?) {
        updateState { state ->
            state.copy(imageUrl = newUri)
        }
    }

    fun updateTabIndex(newTabIndex: Int) {
        updateState { state ->
            state.copy(tabIndex = newTabIndex)
        }
    }

    fun updatePrompt(newPrompt: String) {
        updateState { state ->
            state.copy(prompt = newPrompt)
        }
    }

    fun updateSelectedStyle(styleModel: StyleModel) {
        updateState { state ->
            state.copy(selectedStyle = styleModel)
        }
    }

    fun isCurrentImageValid() = uiState.value.imageUrl != null

    fun generateImage(onSuccess: () -> Unit) {
        updateState {
            it.copy(generatingState = BaseUIState.Loading)
        }
        viewModelScope.launch {
            savedStateHandle[SaveStateConstants.KEY_IMAGE_URI] = uiState.value.imageUrl
        }
    }

}



