package io.github.aungthiha.snackbar

import android.content.Context
import androidx.annotation.StringRes

sealed interface SnackbarText {
    @JvmInline
    value class Literal(val value: String) : SnackbarText

    @JvmInline
    value class Resource(@param:StringRes val value: Int) : SnackbarText
}

/**
 * Creates a [SnackbarText.Literal] from a raw [String].
 *
 * Allows developers to instantiate either a [SnackbarText.Literal] or [SnackbarText.Resource] \
 * using the same `SnackbarText(...)` function name, \
 * improving discoverability and reducing cognitive overhead.
 */
fun SnackbarText(value: String) = SnackbarText.Literal(value)

/**
 * Creates a [SnackbarText.Resource] from a string resource ID.
 *
 * Allows developers SnackbarText instantiate either a [SnackbarText.Literal] or [SnackbarText.Resource] \
 * using the same `SnackbarText(...)` function name, \
 * improving discoverability and reducing cognitive overhead.
 */
fun SnackbarText(@StringRes value: Int) = SnackbarText.Resource(value)

fun SnackbarText.unpackString(context: Context): String = when (this) {
    is SnackbarText.Literal -> value
    is SnackbarText.Resource -> context.getString(value)
}
