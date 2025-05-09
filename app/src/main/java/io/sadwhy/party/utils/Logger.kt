package io.sadwhy.party.utils

import android.util.Log
import io.sadwhy.party.Party
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Simple logger that writes to both logcat and a file
 * @param message The message to log
 */
fun log(message: String) {
    Log.d("PartyApp", message)
    
    // Write to file
    try {
        // Make sure app context is initialized
        if (::Party.appContext.isInitialized) {
            val logFile = File(Party.appContext.filesDir, "app_log.txt")
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            FileWriter(logFile, true).use { writer ->
                writer.append("[$timestamp] $message\n")
            }
        } else {
            Log.e("PartyApp", "Cannot write to log file: appContext not initialized")
        }
    } catch (e: Exception) {
        // If file logging fails, at least log the error to Logcat
        Log.e("PartyApp", "Error writing to log file: ${e.message}", e)
    }
}