package com.apero.aperoaiart.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.apero.aperoaiart.ui.theme.AppTypography
import com.apero.aperoaiart.ui.theme.pxToDp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class SnackBarType(val color: Color) {
    DOWNLOAD_SUCCESS(Color(0xFF4CAF50)),
    ERROR_HAPPEN(Color(0xFFF44336))
}

@Composable
fun AppSnackBarHost(
    modifier: Modifier = Modifier,
    hostState: SnackbarHostState
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier.padding(vertical = 16.pxToDp())
    ) { data ->
        val (typeName, actualMessage) = data.visuals.message.split("::").let {
            it[0] to it.getOrElse(1) { "" }
        }

        val type = SnackBarType.entries.find { it.name == typeName } ?: return@SnackbarHost
        Snackbar(
            containerColor = type.color,
            contentColor = Color.White,
        ) {
            Text(
                text = actualMessage,
                textAlign = TextAlign.Start,
                style = AppTypography.SnackBarText
            )
        }
    }
}

private fun String.toSnackBarTypeOrNull(): SnackBarType? {
    return SnackBarType.entries.find { it.name == this }
}

@Composable
fun rememberAppSnackBarState(): SnackbarHostState {
    return remember { SnackbarHostState() }
}

class AppSnackBarController(
    private val hostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope
) {
    fun show(
        type: SnackBarType,
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        coroutineScope.launch {
            hostState.showSnackbar(
                message = "${type.name}::$message",
                duration = duration
            )
        }
    }
}


// preview without cmt
@Preview(showBackground = true)
@Composable
fun AppSnackBarHostPreview() {
    AppSnackBarHost(
        hostState = rememberAppSnackBarState(),
        modifier = Modifier.fillMaxWidth()
    )
}