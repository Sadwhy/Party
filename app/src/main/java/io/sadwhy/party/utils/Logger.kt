package io.sadwhy.party.utils

import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun logToFile(message: String, fileName: String? = "log.txt") {
    val context = Party.appContext
    val logFile = File(context.filesDir, fileName)
    val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

    try {
        FileWriter(logFile, true).use { writer ->
            writer.append("[$timestamp] $message\n")
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}