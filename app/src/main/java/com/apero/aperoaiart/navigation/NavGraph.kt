package com.apero.aperoaiart.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.apero.aperoaiart.ui.screen.pickphoto.PickPhotoScreen
import com.apero.aperoaiart.ui.screen.result.ResultScreen
import com.apero.aperoaiart.ui.screen.style.StyleScreen

fun NavGraphBuilder.styleScreen(
    onGenerateSuccess: (resultUrl: String) -> Unit,
    onOpenPickPhoto: () -> Unit
) {
    composable<StyleRoute> { backStackEntry ->
        val fileUrl = backStackEntry.arguments?.getString("fileUrl")
        StyleScreen(
            initFileUrl = fileUrl,
            onGenerateSuccess = onGenerateSuccess,
            onOpenPickPhoto = onOpenPickPhoto,
        )
    }
}

fun NavGraphBuilder.pickPhotoScreen(
    onBack: () -> Unit,
    onNext: (selectedUri: String) -> Unit,
) {
    composable<PickPhotoRoute> {
        PickPhotoScreen(
            onBack = onBack,
            onNext = onNext,
        )
    }
}

fun NavGraphBuilder.resultScreen(
    onBack: () -> Unit,
) {
    composable<ResultRoute> { backStackEntry ->
        val resultUrl = backStackEntry.arguments?.getString("resultUrl")
        ResultScreen(
            onBack = onBack
        )
    }
}

fun NavController.navigationToStyle(
    fileUrl: String? = null,
) {
    navigate(StyleRoute(fileUrl), navOptions {
        launchSingleTop = true
    })
}

fun NavController.navigationToPickPhoto() {
    navigate(PickPhotoRoute, navOptions { launchSingleTop = true})
}

fun NavController.navigationToResult(
    resultUrl: String,
) {
    navigate(ResultRoute(resultUrl), navOptions { launchSingleTop = true})
}