package com.apero.aperoaiart.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.apero.aperoaiart.ui.screen.pickphoto.PickPhotoScreen
import com.apero.aperoaiart.ui.screen.pickphoto.PickPhotoViewModel
import com.apero.aperoaiart.ui.screen.result.ResultScreen
import com.apero.aperoaiart.ui.screen.style.StyleScreen
import com.apero.aperoaiart.ui.screen.style.StyleViewModel

fun NavGraphBuilder.styleScreen(
    styleViewModel: StyleViewModel,
    onGenerateSuccess: (resultUrl: String) -> Unit,
    onOpenPickPhoto: () -> Unit
) {
    composable<StyleRoute> { backStackEntry ->
        val fileUrl = backStackEntry.arguments?.getString("fileUrl")
        StyleScreen(
            initFileUrl = fileUrl,
            onGenerateSuccess = onGenerateSuccess,
            onOpenPickPhoto = onOpenPickPhoto,
            viewModel = styleViewModel
        )
    }
}

fun NavGraphBuilder.pickPhotoScreen(
    pickPhotoViewModel: PickPhotoViewModel,
    onBack: () -> Unit,
    onNext: (selectedUri: String) -> Unit,
) {
    composable<PickPhotoRoute> {
        PickPhotoScreen(
            onBack = onBack,
            onNext = onNext,
            viewModel = pickPhotoViewModel,
        )
    }
}

fun NavGraphBuilder.resultScreen(
    onBack: () -> Unit,
) {
    composable<ResultRoute> { backStackEntry ->
        val resultUrl = backStackEntry.arguments?.getString("resultUrl")
        ResultScreen(
            onBack = onBack,
            initUrl = resultUrl ?: "",
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