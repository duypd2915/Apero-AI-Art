package com.apero.aperoaiart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.apero.aperoaiart.ui.screen.result.ResultScreen
import com.apero.aperoaiart.ui.theme.AperoAiArtTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AperoAiArtTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    StyleScreen(
//                        modifier = Modifier.padding(innerPadding)
//                    )
                    ResultScreen(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

