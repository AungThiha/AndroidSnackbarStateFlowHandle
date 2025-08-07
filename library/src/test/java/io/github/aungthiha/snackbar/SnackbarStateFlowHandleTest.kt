package io.github.aungthiha.snackbar

import androidx.compose.material3.SnackbarDuration
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SnackbarStateFlowHandleTest {

    private val snackbarStateFlowHandle = SnackbarStateFlowHandle()

    @Test
    fun `WHEN showSnackBar is called with string resource and string resource action, THEN adds SnackbarModel to flow`() =
        runTest {
            // Arrange
            val onActionPerform = mockk<() -> Unit>(relaxed = true)
            val onDismiss = mockk<() -> Unit>(relaxed = true)

            // Act
            snackbarStateFlowHandle.showSnackBar(
                message = 123,
                actionLabel = 456,
                withDismissAction = true,
                duration = SnackbarDuration.Long,
                onActionPerform = onActionPerform,
                onDismiss = onDismiss
            )

            // Assert
            val messages = snackbarStateFlowHandle.snackbarStateFlow.first()
            assertEquals(1, messages.size)
            val snackbarModel = messages.first()
            assertTrue(snackbarModel.message is SnackbarText.Resource)
            assertEquals(123, (snackbarModel.message as SnackbarText.Resource).value)
            assertTrue(snackbarModel.actionLabel is SnackbarText.Resource)
            assertEquals(456, (snackbarModel.actionLabel as SnackbarText.Resource).value)
            assertTrue(snackbarModel.withDismissAction)
            assertEquals(SnackbarDuration.Long, snackbarModel.duration)
            assertEquals(onActionPerform, snackbarModel.onActionPerform)
            assertEquals(onDismiss, snackbarModel.onDismiss)
        }

    @Test
    fun `WHEN showSnackBar is called with string resource and string literal action, THEN adds SnackbarModel to flow`() =
        runTest {
            // Arrange
            val onActionPerform = mockk<() -> Unit>(relaxed = true)
            val onDismiss = mockk<() -> Unit>(relaxed = true)

            // Act
            snackbarStateFlowHandle.showSnackBar(
                message = 123,
                actionLabel = "Action",
                withDismissAction = false,
                duration = SnackbarDuration.Short,
                onActionPerform = onActionPerform,
                onDismiss = onDismiss
            )

            // Assert
            val messages = snackbarStateFlowHandle.snackbarStateFlow.first()
            assertEquals(1, messages.size)
            val snackbarModel = messages.first()
            assertTrue(snackbarModel.message is SnackbarText.Resource)
            assertEquals(123, (snackbarModel.message as SnackbarText.Resource).value)
            assertTrue(snackbarModel.actionLabel is SnackbarText.Literal)
            assertEquals("Action", (snackbarModel.actionLabel as SnackbarText.Literal).value)
            assertEquals(false, snackbarModel.withDismissAction)
            assertEquals(SnackbarDuration.Short, snackbarModel.duration)
        }

    @Test
    fun `WHEN showSnackBar is called with string literal and string literal action, THEN adds SnackbarModel to flow`() =
        runTest {
            // Arrange
            val onActionPerform = mockk<() -> Unit>(relaxed = true)
            val onDismiss = mockk<() -> Unit>(relaxed = true)

            // Act
            snackbarStateFlowHandle.showSnackBar(
                message = "Test message",
                actionLabel = "Test action",
                withDismissAction = true,
                duration = SnackbarDuration.Indefinite,
                onActionPerform = onActionPerform,
                onDismiss = onDismiss
            )

            // Assert
            val messages = snackbarStateFlowHandle.snackbarStateFlow.first()
            assertEquals(1, messages.size)
            val snackbarModel = messages.first()
            assertTrue(snackbarModel.message is SnackbarText.Literal)
            assertEquals("Test message", (snackbarModel.message as SnackbarText.Literal).value)
            assertTrue(snackbarModel.actionLabel is SnackbarText.Literal)
            assertEquals("Test action", (snackbarModel.actionLabel as SnackbarText.Literal).value)
            assertTrue(snackbarModel.withDismissAction)
            assertEquals(SnackbarDuration.Indefinite, snackbarModel.duration)
        }

    @Test
    fun `WHEN showSnackBar is called with string literal and string resource action, THEN adds SnackbarModel to flow`() =
        runTest {
            // Arrange
            val onActionPerform = mockk<() -> Unit>(relaxed = true)
            val onDismiss = mockk<() -> Unit>(relaxed = true)

            // Act
            snackbarStateFlowHandle.showSnackBar(
                message = "Test message",
                actionLabel = 789,
                withDismissAction = false,
                duration = SnackbarDuration.Short,
                onActionPerform = onActionPerform,
                onDismiss = onDismiss
            )

            // Assert
            val messages = snackbarStateFlowHandle.snackbarStateFlow.first()
            assertEquals(1, messages.size)
            val snackbarModel = messages.first()
            assertTrue(snackbarModel.message is SnackbarText.Literal)
            assertEquals("Test message", (snackbarModel.message as SnackbarText.Literal).value)
            assertTrue(snackbarModel.actionLabel is SnackbarText.Resource)
            assertEquals(789, (snackbarModel.actionLabel as SnackbarText.Resource).value)
            assertEquals(false, snackbarModel.withDismissAction)
            assertEquals(SnackbarDuration.Short, snackbarModel.duration)
        }

    @Test
    fun `WHEN showSnackBar is called with null action label, THEN creates SnackbarModel with null actionLabel`() =
        runTest {
            // Arrange & Act
            snackbarStateFlowHandle.showSnackBar(
                message = "Test message",
                actionLabel = null,
                withDismissAction = false,
                duration = SnackbarDuration.Short
            )

            // Assert
            val messages = snackbarStateFlowHandle.snackbarStateFlow.first()
            assertEquals(1, messages.size)
            val snackbarModel = messages.first()
            assertTrue(snackbarModel.message is SnackbarText.Literal)
            assertEquals("Test message", (snackbarModel.message as SnackbarText.Literal).value)
            assertEquals(null, snackbarModel.actionLabel)
        }

    @Test
    fun `WHEN showSnackBar is called multiple times, THEN adds multiple SnackbarModels to flow`() =
        runTest {
            // Arrange & Act
            snackbarStateFlowHandle.showSnackBar(
                message = "First message",
                actionLabel = "First action"
            )

            snackbarStateFlowHandle.showSnackBar(
                message = "Second message",
                actionLabel = "Second action"
            )

            // Assert
            val messages = snackbarStateFlowHandle.snackbarStateFlow.first()
            assertEquals(2, messages.size)
            assertEquals("First message", (messages[0].message as SnackbarText.Literal).value)
            assertEquals("Second message", (messages[1].message as SnackbarText.Literal).value)
        }

    @Test
    fun `WHEN multiple snackbars are added and middle one is removed, THEN removes correct model`() =
        runTest {
            // Arrange
            snackbarStateFlowHandle.showSnackBar(
                message = "First message",
                actionLabel = "First action"
            )

            snackbarStateFlowHandle.showSnackBar(
                message = "Second message",
                actionLabel = "Second action"
            )

            snackbarStateFlowHandle.showSnackBar(
                message = "Third message",
                actionLabel = "Third action"
            )

            val messagesBeforeRemoval = snackbarStateFlowHandle.snackbarStateFlow.first()
            assertEquals(3, messagesBeforeRemoval.size)

            // Act
            val middleModel = messagesBeforeRemoval[1]
            snackbarStateFlowHandle.snackbarShown(middleModel)

            // Assert
            val messagesAfterRemoval = snackbarStateFlowHandle.snackbarStateFlow.first()
            assertEquals(2, messagesAfterRemoval.size)
            assertEquals(
                "First message",
                (messagesAfterRemoval[0].message as SnackbarText.Literal).value
            )
            assertEquals(
                "Third message",
                (messagesAfterRemoval[1].message as SnackbarText.Literal).value
            )
        }

    @Test
    fun `WHEN snackbar queue follows FIFO order, THEN maintains insertion order`() =
        runTest {
            // Arrange & Act
            snackbarStateFlowHandle.showSnackBar(
                message = "Message 1",
                actionLabel = "Action 1"
            )

            snackbarStateFlowHandle.showSnackBar(
                message = "Message 2",
                actionLabel = "Action 2"
            )

            snackbarStateFlowHandle.showSnackBar(
                message = "Message 3",
                actionLabel = "Action 3"
            )

            // Assert
            val messages = snackbarStateFlowHandle.snackbarStateFlow.first()
            assertEquals(3, messages.size)
            assertEquals("Message 1", (messages[0].message as SnackbarText.Literal).value)
            assertEquals("Message 2", (messages[1].message as SnackbarText.Literal).value)
            assertEquals("Message 3", (messages[2].message as SnackbarText.Literal).value)
        }

    @Test
    fun `WHEN snackbarShown is called with existing model, THEN removes model from flow`() =
        runTest {
            // Arrange
            snackbarStateFlowHandle.showSnackBar(
                message = "Test message",
                actionLabel = "Test action"
            )

            val messagesBeforeConsumption = snackbarStateFlowHandle.snackbarStateFlow.first()
            assertEquals(1, messagesBeforeConsumption.size)

            // Act
            val consumedModel = messagesBeforeConsumption.first()
            snackbarStateFlowHandle.snackbarShown(consumedModel)

            // Assert
            val messagesAfterConsumption = snackbarStateFlowHandle.snackbarStateFlow.first()
            assertTrue(messagesAfterConsumption.isEmpty())
        }

    @Test
    fun `WHEN snackbarShown is called with non-existing model, THEN flow remains unchanged`() =
        runTest {
            // Arrange
            snackbarStateFlowHandle.showSnackBar(
                message = "Test message",
                actionLabel = "Test action"
            )

            val originalMessages = snackbarStateFlowHandle.snackbarStateFlow.first()
            assertEquals(1, originalMessages.size)

            val nonExistingModel = SnackbarModel(
                message = SnackbarText.Literal("Non-existing"),
                actionLabel = null,
                withDismissAction = false,
                duration = SnackbarDuration.Short,
                onActionPerform = {},
                onDismiss = {}
            )

            // Act
            snackbarStateFlowHandle.snackbarShown(nonExistingModel)

            // Assert
            val messagesAfterRemoval = snackbarStateFlowHandle.snackbarStateFlow.first()
            assertEquals(1, messagesAfterRemoval.size)
            assertEquals(
                "Test message",
                (messagesAfterRemoval.first().message as SnackbarText.Literal).value
            )
        }

    @Test
    fun `WHEN no snackbars are shown, THEN flow emits empty list`() = runTest {
        // Act & Assert
        val messages = snackbarStateFlowHandle.snackbarStateFlow.first()
        assertTrue(messages.isEmpty())
    }

    @Test
    fun `WHEN snackbarStateFlow property is accessed, THEN returns the controller itself`() {
        // Act & Assert
        assertEquals(snackbarStateFlowHandle, snackbarStateFlowHandle.snackbarStateFlow)
    }

    @Test
    fun `WHEN showSnackBar is called with string resource and null action label, THEN creates SnackbarModel with null actionLabel`() =
        runTest {
            // Arrange & Act
            snackbarStateFlowHandle.showSnackBar(
                message = 123,
                actionLabel = null as Int?,
                withDismissAction = false,
                duration = SnackbarDuration.Short
            )

            // Assert
            val messages = snackbarStateFlowHandle.snackbarStateFlow.first()
            assertEquals(1, messages.size)
            val snackbarModel = messages.first()
            assertTrue(snackbarModel.message is SnackbarText.Resource)
            assertEquals(123, (snackbarModel.message as SnackbarText.Resource).value)
            assertEquals(null, snackbarModel.actionLabel)
        }

    @Test
    fun `WHEN showSnackBar is called with default parameters, THEN uses correct default values`() =
        runTest {
            // Arrange & Act
            snackbarStateFlowHandle.showSnackBar(
                message = "Test message"
            )

            // Assert
            val messages = snackbarStateFlowHandle.snackbarStateFlow.first()
            assertEquals(1, messages.size)
            val snackbarModel = messages.first()
            assertTrue(snackbarModel.message is SnackbarText.Literal)
            assertEquals("Test message", (snackbarModel.message as SnackbarText.Literal).value)
            assertEquals(null, snackbarModel.actionLabel)
            assertEquals(false, snackbarModel.withDismissAction)
            assertEquals(SnackbarDuration.Short, snackbarModel.duration)
        }

    @Test
    fun `WHEN showSnackBar is called with action label, THEN uses Indefinite duration by default`() =
        runTest {
            // Arrange & Act
            snackbarStateFlowHandle.showSnackBar(
                message = "Test message",
                actionLabel = "Action"
            )

            // Assert
            val messages = snackbarStateFlowHandle.snackbarStateFlow.first()
            assertEquals(1, messages.size)
            val snackbarModel = messages.first()
            assertEquals(SnackbarDuration.Indefinite, snackbarModel.duration)
        }
}
