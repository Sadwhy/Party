package io.sadwhy.party.core.ui.composable.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class ConfirmationDialog(
    val icon: ImageVector? = null,
    val title: String,
    val message: String,
    val confirmButtonText: String = "Confirm",
    val onConfirm: () -> Unit,
) : DialogContent

@Composable
internal fun ConfirmationDialogContent(content: ConfirmationDialog) {
    AlertDialog(
        onDismissRequest = { DialogController.dismiss() },
        icon = {
            content.icon?.let {
                Icon(imageVector = it, contentDescription = "Dialog Icon")
            }
        },
        title = { Text(text = content.title) },
        text = { Text(text = content.message) },
        confirmButton = {
            TextButton(
                onClick = {
                    content.onConfirm()
                    DialogController.dismiss()
                }
            ) {
                Text(content.confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = { DialogController.dismiss() }) {
                Text("Cancel")
            }
        }
    )
}
