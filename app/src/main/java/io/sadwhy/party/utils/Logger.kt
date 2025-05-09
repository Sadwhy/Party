package io.sadwhy.party.utils

import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun log(message: String, fileName: String = "log.txt") {
    try {
        val context = Party.appContext
        val logFile = File(context.filesDir, fileName)
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        FileWriter(logFile, true).use { writer ->
            writer.append("[$timestamp] $message\n")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}