package io.sadwhy.party.core.debug.console

import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FloatingConsoleViewModel : ViewModel() {

    private val _isExpanded = MutableStateFlow(false)
    val isExpanded = _isExpanded.asStateFlow()

    private val _logs = MutableStateFlow<List<DebugLogger.LogEntry>>(emptyList())
    val logs = _logs.asStateFlow()

    private val _offset = MutableStateFlow(IntOffset(0, 100))
    val offset = _offset.asStateFlow()

    init {
        viewModelScope.launch {
            DebugLogger.logFlow.collect { newLog ->
                _logs.value = (_logs.value + newLog).takeLast(100) // Keep the list size manageable
            }
        }
    }

    fun toggleExpansion() {
        _isExpanded.value = !_isExpanded.value
    }

    fun onDrag(dragAmount: IntOffset) {
        _offset.value += dragAmount
    }
}
