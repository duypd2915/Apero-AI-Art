package com.apero.aperoaiart.data

import com.duyhellowolrd.ai_art_service.network.response.StyleItem

data class StyleModel(
    val id: String,
    val name: String,
    val image: String
)

fun StyleItem.toModel(): StyleModel {
    return StyleModel(
        id = id,
        name = name,
        image = key
    )
}