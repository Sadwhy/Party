package io.sadwhy.party.core.ui.composable.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

/**
 * A central composable responsible for displaying the correct dialog
 * based on the state in the DialogController.
 *
 * Place this in the Scaffold at main screen.
 */
@Composable
fun DialogHandler() {
    val currentDialog by DialogController.currentDialog

    when (val dialog = currentDialog) {
        is UrlDialog -> UrlDialogContent(content = dialog)
        is ConfirmationDialog -> ConfirmationDialogContent(content = dialog)
        null -> {}
    }
}
