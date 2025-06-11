package com.apero.aperoaiart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.apero.aperoaiart.navigation.StyleRoute
import com.apero.aperoaiart.navigation.navigationToResult
import com.apero.aperoaiart.navigation.navigationToStyle
import com.apero.aperoaiart.navigation.pickPhotoScreen
import com.apero.aperoaiart.navigation.resultScreen
import com.apero.aperoaiart.navigation.styleScreen
import com.apero.aperoaiart.ui.theme.AperoAiArtTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()
            AperoAiArtTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navHostController,
                        startDestination = StyleRoute,
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        styleScreen(
                            onGenerateSuccess = {
                                navHostController.navigationToResult()
                            }
                        )

                        pickPhotoScreen()

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
}

