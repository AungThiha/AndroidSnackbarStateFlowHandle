package io.github.aungthiha.snackbar.demo

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import io.github.aungthiha.snackbar.SnackbarStateFlowHandle
import io.github.aungthiha.snackbar.SnackbarStateFlowOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MainViewModel(
    private val snackbarStateFlowHandle: SnackbarStateFlowHandle = SnackbarStateFlowHandle()
) : ViewModel(), SnackbarStateFlowOwner by snackbarStateFlowHandle {

    private val mutableOnActionPerformCalled: MutableStateFlow<Int> = MutableStateFlow(0)
    val onActionPerformCalled: StateFlow<Int> = mutableOnActionPerformCalled

    private val mutableOnDismissedCalled: MutableStateFlow<Int> = MutableStateFlow(0)
    val onDismissedCalled: StateFlow<Int> = mutableOnDismissedCalled

    fun snackbarWithStringResource() {
        snackbarStateFlowHandle.showSnackBar(
            message = R.string.hello_from_AndroidSnackbarStateFlowHandle
        )
    }

    fun snackbarWithStringLiteral() {
        snackbarStateFlowHandle.showSnackBar(
            message = "hey"
        )
    }

    fun snackbarWithMixedStringTypes() {
        snackbarStateFlowHandle.showSnackBar(
            message = "မင်္ဂလာပါ",
            actionLabel = R.string.ok,
        )
    }

    fun snackbarWithAction() {
        snackbarStateFlowHandle.showSnackBar(
            message = R.string.hello_from_AndroidSnackbarStateFlowHandle,
            actionLabel = R.string.ok
        )
    }

    fun snackbarWithDismissAction() {
        snackbarStateFlowHandle.showSnackBar(
            message = R.string.hello_from_AndroidSnackbarStateFlowHandle,
            withDismissAction = true
        )
    }

    fun snackbarWithOnActionPerformCallback() {
        snackbarStateFlowHandle.showSnackBar(
            message = R.string.hello_from_AndroidSnackbarStateFlowHandle,
            actionLabel = R.string.ok,
            onActionPerform = {
                mutableOnActionPerformCalled.update { it + 1 }
            }
        )
    }

    fun snackbarWithOnDismissCallback() {
        snackbarStateFlowHandle.showSnackBar(
            message = R.string.hello_from_AndroidSnackbarStateFlowHandle,
            onDismiss = {
                mutableOnDismissedCalled.update { it + 1 }
            }
        )
    }

    fun indefiniteSnackbar() {
        snackbarStateFlowHandle.showSnackBar(
            message = R.string.hello_from_AndroidSnackbarStateFlowHandle,
            withDismissAction = true,
            duration = SnackbarDuration.Indefinite
        )
    }
}
