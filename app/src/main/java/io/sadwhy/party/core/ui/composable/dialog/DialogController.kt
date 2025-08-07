package io.sadwhy.party.core.ui.composable.dialog

import androidx.compose.runtime.mutableStateOf

/**
 * A singleton controller to manage the display of dialogs throughout the application.
 * It holds the state for the currently visible dialog.
 */
object DialogController {
    val currentDialog = mutableStateOf<DialogContent?>(null)

    /**
     * Shows a dialog by setting its content.
     * @param content The specific DialogContent data class instance to display.
     */
    fun show(content: DialogContent) {
        currentDialog.value = content
    }

    /**
     * Hides the currently visible dialog.
     */
    fun dismiss() {
        currentDialog.value = null
    }
}
