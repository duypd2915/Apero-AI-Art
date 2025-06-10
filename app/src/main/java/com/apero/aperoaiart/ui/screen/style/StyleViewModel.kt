package com.apero.aperoaiart.ui.screen.style

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.base.BaseViewModel
import com.apero.aperoaiart.data.StyleModel
import com.apero.aperoaiart.data.toModel
import com.apero.aperoaiart.navigation.SaveStateConstants
import com.duyhellowolrd.ai_art_service.data.AiArtRepository
import com.duyhellowolrd.ai_art_service.data.params.AiArtParams
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
                                Log.d("StyleViewModel", "categories: ${data.items.map { it.id }}")
                                Log.d(
                                    "StyleViewModel",
                                    "styles id: ${data.items.flatMap { it.styles.map { it.id } }}"
                                )
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
        Log.d("StyleViewModel", "updateSelectedStyle: ${styleModel.id}")
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
            val uiStateValue = uiState.value
            if (uiStateValue.imageUrl == null) return@launch
            val genResult = aiArtRepository.genArtAi(
                params = AiArtParams(
                    prompt = uiStateValue.prompt,
                    styleId = uiStateValue.selectedStyle?.id ?: "",
                    positivePrompt = uiStateValue.prompt,
                    negativePrompt = uiStateValue.prompt,
                    imageUri = uiStateValue.imageUrl
                )
            )
            genResult.fold(
                onSuccess = {
                    Log.d("StyleViewModel", "generateImage: ok")
                    savedStateHandle[SaveStateConstants.KEY_IMAGE_URI] = uiState.value.imageUrl
                    onSuccess()
                },
                onFailure = {
                    Log.e("StyleViewModel", "generateImage: error", it)
                }
            )

        }
    }
}



