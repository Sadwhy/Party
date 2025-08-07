package io.sadwhy.party.core.ui.composable.dialog

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import io.sadwhy.party.R

data class UrlDialog(val url: String) : DialogContent

@Composable
internal fun UrlDialogContent(content: UrlDialog) {
    val context = LocalContext.current

    val onConfirm = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(content.url))
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "No application can handle this request. Please install a web browser.",
                Toast.LENGTH_LONG
            ).show()
        }
        DialogController.dismiss()
    }

    AlertDialog(
        onDismissRequest = { DialogController.dismiss() },
        icon = { Icon(painterResource(id = R.drawable.open_in_browser), "Link Icon") },
        title = { Text("Open External Link?") },
        text = { Text("You are about to open the following link. Do you trust this destination?\n\n${content.url}") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Open")
            }
        },
        dismissButton = {
            TextButton(onClick = { DialogController.dismiss() }) {
                Text("Cancel")
            }
        }
    )
}
