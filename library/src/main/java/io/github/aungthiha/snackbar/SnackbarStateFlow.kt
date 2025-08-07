package io.github.aungthiha.snackbar

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow

interface SnackbarStateFlow : Flow<List<SnackbarModel>> {
    fun snackbarShown(snackbarModel: SnackbarModel)
}

@SuppressLint("ComposableNaming")
@Composable
fun SnackbarStateFlow.observeWithLifecycle(
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    consumeSnackbarModel: suspend (SnackbarModel) -> Unit,
) {
    LaunchedEffect(this) {
        flowWithLifecycle(lifecycle, minActiveState)
            .collect { snackbarMessages ->
                if (snackbarMessages.isNotEmpty()) {
                    val snackbarMessage = snackbarMessages.first()
                    consumeSnackbarModel(snackbarMessage)
                    snackbarShown(snackbarMessage)
                }
            }
    }
}
