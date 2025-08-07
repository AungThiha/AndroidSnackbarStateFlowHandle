package io.github.aungthiha.snackbar

import androidx.compose.material3.SnackbarDuration

class SnackbarModel(
    val message: SnackbarText,
    val actionLabel: SnackbarText?,
    val withDismissAction: Boolean,
    val duration: SnackbarDuration,
    val onActionPerform: () -> Unit,
    val onDismiss: () -> Unit,
)
