package com.apero.aperoaiart.navigation

import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

sealed class NavigationItem(val route: KClass<*>) {
    data object Style : NavigationItem(StyleRoute::class)
    data object PickPhoto : NavigationItem(PickPhotoRoute::class)
    data object Result : NavigationItem(ResultRoute::class)
}

@Serializable
data object ResultRoute

@Serializable
data object PickPhotoRoute

@Serializable
data object StyleRoute