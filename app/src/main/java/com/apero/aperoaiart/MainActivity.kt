package com.apero.aperoaiart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.apero.aperoaiart.navigation.StyleRoute
import com.apero.aperoaiart.navigation.navigationToPickPhoto
import com.apero.aperoaiart.navigation.navigationToResult
import com.apero.aperoaiart.navigation.navigationToStyle
import com.apero.aperoaiart.navigation.pickPhotoScreen
import com.apero.aperoaiart.navigation.resultScreen
import com.apero.aperoaiart.navigation.styleScreen
import com.apero.aperoaiart.ui.theme.AperoAiArtTheme
import com.apero.aperoaiart.ui.theme.pxToDp
import com.apero.aperoaiart.utils.UiConstant
import com.apero.aperoaiart.utils.hideNavigationBar
import com.apero.aperoaiart.utils.transparentStatusBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        hideNavigationBar()
        transparentStatusBar()
        setContent {
            val configuration = LocalConfiguration.current
            val navHostController = rememberNavController()
            val imageSizePx =
                remember(configuration.screenWidthDp) { (configuration.screenWidthDp - 60) / 3 }
            UiConstant.preferImageSize = imageSizePx
            AperoAiArtTheme {
                NavHost(
                    navController = navHostController,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 23.pxToDp()),
                    startDestination = StyleRoute(),
                ) {
                    styleScreen(
                        onGenerateSuccess = { fileUrl ->
                            navHostController.navigationToResult(fileUrl)
                        },
                        onOpenPickPhoto = {
                            navHostController.navigationToPickPhoto()
                        }
                    )

                    pickPhotoScreen(
                        onBack = {
                            navHostController.popBackStack()
                        },
                        onNext = { selectedUri ->
                            navHostController.navigationToStyle(fileUrl = selectedUri)
                        }
                    )

                    resultScreen(
                        onBack = {
                            navHostController.navigationToStyle()
                        }
                    )
                }
            }
        }
    }
}

