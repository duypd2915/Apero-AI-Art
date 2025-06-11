package com.apero.aperoaiart.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.apero.aperoaiart.ui.screen.result.ResultScreen
import com.apero.aperoaiart.ui.screen.style.StyleScreen

fun NavGraphBuilder.styleScreen(
    onGenerateSuccess: (resultUrl: String) -> Unit,
) {
    composable<StyleRoute> {
        StyleScreen(
            onGenerateSuccess = onGenerateSuccess,
        )
    }
}

fun NavGraphBuilder.pickPhotoScreen() {
    composable<PickPhotoRoute> {
        Text("Pick Photo Screen")
    }
}

fun NavGraphBuilder.resultScreen(
    onBack: () -> Unit,
) {
    composable<ResultRoute> {
        ResultScreen()
    }
}

fun NavController.navigationToStyle(
) {
    navigate(StyleRoute, navOptions {
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