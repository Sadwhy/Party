package io.sadwhy.party.presentation.crash

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import io.sadwhy.party.MainActivity

class CrashActivity : ComponentActivity() {

    private val clipboardManager by lazy {
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    private lateinit var crashLog: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crashLog = intent.getStringExtra("crash_info") ?: "No crash info."

        setContent {
            MaterialTheme {
                CrashScreen(crashLog)
            }
        }
    }

    @Composable
    fun CrashScreen(crashLog: String) {
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("App Crashed", style = MaterialTheme.typography.headlineLarge)
            Text("Something went wrong. Here’s what we caught:")

            SelectionContainer {
                Text(
                    text = crashLog,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFEEEEEE))
                        .padding(8.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    clipboardManager.setPrimaryClip(
                        ClipData.newPlainText("Crash Log", crashLog)
                    )
                }) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Copy Log")
                }

                Button(onClick = {
                    val intent = Intent(context, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    }
                    context.startActivity(intent)
                    (context as? Activity)?.finish()
                }) {
                    Text("Restart App")
                }
            }
        }
    }
}