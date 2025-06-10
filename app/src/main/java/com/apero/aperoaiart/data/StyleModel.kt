package com.apero.aperoaiart.data

import com.duyhellowolrd.ai_art_service.network.response.CategoryItem
import com.duyhellowolrd.ai_art_service.network.response.StyleDetail

data class CategoryModel(
    val id: String,
    val name: String,
    val styles: List<StyleModel> = emptyList()
)
data class StyleModel(
    val id: String,
    val name: String,
    val image: String,
    val positivePrompt: String = "",
    val negativePrompt: String = ""
)

fun CategoryItem.toModel(): CategoryModel {
    return CategoryModel(
        id = id,
        name = name,
        styles = styles.map { it.toModel() }
    )
}

fun StyleDetail.toModel(): StyleModel = StyleModel(
    id = id,
    name = name,
    image = key,
    positivePrompt = config.positivePrompt ?: "",
    negativePrompt = config.negativePrompt ?: ""
)