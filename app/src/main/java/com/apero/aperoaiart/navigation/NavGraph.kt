package com.apero.aperoaiart.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.apero.aperoaiart.ui.screen.pickphoto.PickPhotoScreen
import com.apero.aperoaiart.ui.screen.pickphoto.PickPhotoViewModel
import com.apero.aperoaiart.ui.screen.result.ResultScreen
import com.apero.aperoaiart.ui.screen.style.StyleScreen

fun NavGraphBuilder.styleScreen(
    onGenerateSuccess: (resultUrl: String) -> Unit,
    onOpenPickPhoto: () -> Unit
) {
    composable<StyleRoute> {
        StyleScreen(
            onGenerateSuccess = onGenerateSuccess,
            onOpenPickPhoto = onOpenPickPhoto,
        )
    }
}

fun NavGraphBuilder.pickPhotoScreen(
    onBack: () -> Unit,
    onNext: (selectedUri: String) -> Unit,
    pickPhotoViewModel: PickPhotoViewModel
) {
    composable<PickPhotoRoute> {
        PickPhotoScreen(
            onBack = onBack,
            onNext = onNext,
            viewModel = pickPhotoViewModel
        )
    }
}

fun NavGraphBuilder.resultScreen(
    onBack: () -> Unit,
) {
    composable<ResultRoute> {
        ResultScreen(
            onBack = onBack
        )
    }
}

fun NavController.navigationToStyle(
    fileUrl: String? = null,
) {
    navigate(StyleRoute(fileUrl), navOptions {
        restoreState = true
    })
}

fun NavController.navigationToPickPhoto() {
    navigate(PickPhotoRoute, navOptions {
        restoreState = false
    })
}

fun NavController.navigationToResult(
    resultUrl: String,
) {
    navigate(ResultRoute(resultUrl), navOptions {
        restoreState = false
    })
}