package io.sadwhy.party.core.debug

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Logger {
    data class LogEntry(
        val message: String,
        val level: LogLevel,
        val timestamp: String = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
    )

    enum class LogLevel { INFO, WARN, ERROR, SUCCESS }

    private val _logFlow = MutableSharedFlow<LogEntry>(replay = 100) // Replay last 100 logs
    val logFlow = _logFlow.asSharedFlow()

    fun log(message: String, level: LogLevel = LogLevel.INFO) {
        _logFlow.tryEmit(LogEntry(message, level))
    }
}
