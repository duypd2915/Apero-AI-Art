package com.apero.aperoaiart.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.apero.aperoaiart.ui.theme.pxToDp

@Composable
fun SnackBar(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState
) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier
            .padding(16.pxToDp())
    ) { data ->
        val visuals = data.visuals
        val type = when {
            visuals.message.contains(SnackBarType.ERROR.prefix, true) -> SnackBarType.ERROR
            visuals.message.contains(SnackBarType.SUCCESS.prefix, true) -> SnackBarType.SUCCESS
            else -> SnackBarType.INFO
        }

        val backgroundColor = when (type) {
            SnackBarType.SUCCESS -> Color(0xFF4CAF50)
            SnackBarType.ERROR -> Color(0xFFF44336)
            SnackBarType.INFO -> Color(0xFF2196F3)
        }

        Snackbar(
            containerColor = backgroundColor,
            contentColor = Color.White,
            action = {
                visuals.actionLabel?.let { label ->
                    Text(
                        text = "",
                        modifier = Modifier.padding(8.pxToDp())
                    )
                }
            }
        ) {
            Text(text = visuals.message.removePrefix(type.prefix))
        }
    }
}

enum class SnackBarType(val prefix: String) {
    SUCCESS("[success]"),
    ERROR("[error]"),
    INFO("")
}

@Composable
fun rememberAppSnackBar(): SnackbarHostState {
    return remember { SnackbarHostState() }
}