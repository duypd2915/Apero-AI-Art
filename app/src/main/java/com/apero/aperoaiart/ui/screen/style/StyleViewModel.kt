package com.apero.aperoaiart.ui.screen.style

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.base.BaseViewModel
import com.apero.aperoaiart.data.StyleModel
import com.apero.aperoaiart.data.toModel
import com.apero.aperoaiart.navigation.StyleRoute
import com.apero.aperoaiart.utils.getArgOrNull
import com.apero.aperoaiart.utils.isNotNullOrEmpty
import com.duyhellowolrd.ai_art_service.data.AiArtRepository
import com.duyhellowolrd.ai_art_service.data.params.AiArtParams
import com.duyhellowolrd.ai_art_service.exception.AiArtException
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

    fun loadUriFromNavigation() {
        updateState { state ->
            state.copy(
                imageUrl = savedStateHandle.getArgOrNull<String>(StyleRoute.KEY_FILE_URL)?.toUri()
            )
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

    fun isCurrentImageValid() = uiState.value.imageUrl.isNotNullOrEmpty()

    fun isReadyToGenerate() = isCurrentImageValid() && uiState.value.selectedStyle != null

    fun generateImage(context: Context, onSuccess: (resultUrl: String) -> Unit) {
        updateState {
            it.copy(generatingState = BaseUIState.Loading)
        }
        viewModelScope.launch {
            val uiStateValue = uiState.value
            if (uiStateValue.imageUrl == null) {
                updateState {
                    it.copy(generatingState = BaseUIState.Error("Image is required"))
                }
                return@launch
            }
            if (uiStateValue.selectedStyle == null) {
                updateState {
                    it.copy(generatingState = BaseUIState.Error("Style is required"))
                }
                return@launch
            }
            val genResult = aiArtRepository.genArtAi(
                params = AiArtParams(
                    prompt = uiStateValue.prompt,
                    styleId = uiStateValue.selectedStyle.id,
                    positivePrompt = uiStateValue.prompt,
                    negativePrompt = uiStateValue.prompt,
                    imageUri = uiStateValue.imageUrl
                )
            )
            genResult.fold(
                onSuccess = { fileUrl ->
                    onSuccess(fileUrl)
                },
                onFailure = { error ->
                    val message =
                        if (error is AiArtException) context.getString(error.errorReason.resMessage) else ServiceConstants.UNKNOWN_ERROR_MESSAGE
                    updateState {
                        it.copy(generatingState = BaseUIState.Error(message))
                    }
                }
            )
        }
    }
}



