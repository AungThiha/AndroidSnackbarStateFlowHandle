# AndroidSnackbarStateFlowHandle

A lifecycle-aware Snackbar library that eliminates boilerplate and prevents missed/duplicated snackbars in Jetpack Compose.

---

## Why use AndroidSnackbarStateFlowHandle?

- No more manual management of Snackbar queue
- No more missed or duplicated snackbars
- One-liner API for triggering snackbars from your `ViewModel`
- Lifecycle-aware: events are only collected when the UI is active
- No brittle base classes - favors composition over inheritance using Kotlin delegation
- Converts string resources automatically (e.g. R.string.key → "Actual String")
- Supports all original showSnackbar parameters (action labels, duration, callbacks, etc.)
- Fully unit-testable

---

## Installation

```kotlin
dependencies {
    implementation("io.github.aungthiha-android-snackbar-stateflow-handle-1.0.0")
}
```

---

## Setup

### 1. Add `SnackbarStateFlowHandle` to your `ViewModel`
```kotlin
import io.github.aungthiha.snackbar.SnackbarStateFlowHandle
import io.github.aungthiha.snackbar.SnackbarStateFlowOwner

class MyViewModel(
    private val snackbarStateFlowHandle: SnackbarStateFlowHandle = SnackbarStateFlowHandle()
) : ViewModel(), SnackbarStateFlowOwner by snackbarStateFlowHandle {

    fun showSimpleSnackbar() {
        snackbarStateFlowHandle.showSnackBar(message = R.string.hello_world)
    }
}
```
---

### 2. Observe snackbars in your Composable
```kotlin
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import io.github.aungthiha.snackbar.observeWithLifecycle
import io.github.aungthiha.snackbar.rememberSnackbarHostController

@Composable
fun MyScreen(
    // yes, it's a bad practice to directly pass a ViewModel into a Screen 
    // but this is to make it easy to show how to use the AndroidSnackbarStateFlowHandle
    viewModel: MyViewModel = viewModel()
) {
    
    val snackbarHostController = rememberSnackbarHostController()
    viewModel.snackbarStateFlow.observeWithLifecycle {
        snackbarHostController.showSnackbar(it)
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostController.snackbarHostState) }) {
        Button(onClick = { viewModel.showSimpleSnackbar() }) {
            Text("Show Snackbar")
        }
    }
}
```

---

## API Overview

Use showSnackBar(...) from your ViewModel. You can pass string resources, string literals, or even mix both.
```kotlin
// All parameters
snackbarStateFlowHandle.showSnackBar(
    message = R.string.hello_world, // can be either string resource or String
    actionLabel = "ok", //  can be either string resource or String
    withDismissAction = true,
    duration = SnackbarDuration.Indefinite,
    onActionPerform = { /* handle action */ },
    onDismiss = { /* handle dismiss */ }
)

// Using a string resource
snackbarStateFlowHandle.showSnackBar(
    message = R.string.hello_world,
    actionLabel = R.string.ok
)

// Using a raw string (e.g., from backend or dynamic input)
snackbarStateFlowHandle.showSnackBar(
    message = "Something went wrong!",
    actionLabel = "Retry"
)

// Mixing string types
snackbarStateFlowHandle.showSnackBar(
    message = "မင်္ဂလာပါ",
    actionLabel = R.string.ok
)

snackbarStateFlowHandle.showSnackBar(
    message = R.string.hello_world,
    actionLabel = "ok"
)
```
All parameters are optional except the message.   
For more example usages,
see [MainViewModel.kt](./app/src/main/java/io/github/aungthiha/snackbar/demo/MainViewModel.kt)

---

## Unit Testing

You can test snackbar emissions using `runTest` and collecting from `snackbarStateFlow`.

```kotlin
class MyViewModelTest {

    private val viewModel = MyViewModel()

    @Test
    fun snackbar_is_emitted() = runTest {
        viewModel.showSimpleSnackbar()

        val snackbarModels = viewModel.snackbarStateFlow.first()
        val snackbarModel = snackbarModels.first()

        assertEquals(
            SnackbarText.Resource(R.string.hello_world),
            snackbarModel.message
        )
    }
}
```
---

## Contributing
PRs and feedback welcome!

---

## License
MIT
