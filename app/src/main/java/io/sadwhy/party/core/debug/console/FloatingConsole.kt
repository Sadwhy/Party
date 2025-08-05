package io.sadwhy.party.core.debug.console

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.sadwhy.party.BuildConfig
import io.sadwhy.party.core.debug.Logger
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * A wrapper that displays a floating debug console over the provided content
 * Disabled in release.
 *
 * Usage:
 * FloatingConsole {
 * MainScreen()
 * }
 */
@Composable
fun FloatingConsole(content: @Composable () -> Unit) {
    // TODO: add `BuildConfig.DEBUG` when releasing 
    if (true) {
        val consoleViewModel: FloatingConsoleViewModel = viewModel()

        Box(modifier = Modifier.fillMaxSize()) {
            content()

            FloatingConsoleView(viewModel = consoleViewModel)
        }
    } else {
        content()
    }
}

@Composable
internal fun FloatingConsoleView(viewModel: FloatingConsoleViewModel) {
    val isExpanded by viewModel.isExpanded.collectAsState()
    val offset by viewModel.offset.collectAsState()

    Box(
        modifier = Modifier
            .offset { offset }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.onDrag(
                        IntOffset(dragAmount.x.roundToInt(), dragAmount.y.roundToInt())
                    )
                }
            }
    ) {
        AnimatedVisibility(visible = !isExpanded) {
            FloatingHeadIcon(onClick = { viewModel.toggleExpansion() })
        }
        AnimatedVisibility(visible = isExpanded) {
            val logs by viewModel.logs.collectAsState()
            ConsolePanel(
                logs = logs,
                onClose = { viewModel.toggleExpansion() }
            )
        }
    }
}

@Composable
internal fun FloatingHeadIcon(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .shadow(4.dp, CircleShape)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .size(56.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Terminal,
            contentDescription = "Open Console",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
internal fun ConsolePanel(logs: List<Logger.LogEntry>, onClose: () -> Unit) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            listState.animateScrollToItem(logs.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .widthIn(max = 360.dp)
            .height(400.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2B2B2B))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Debug Console", color = Color.White)
                IconButton(onClick = onClose, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close Console",
                        tint = Color.White
                    )
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                items(logs) { log ->
                    LogItem(log)
                }
            }
        }
    }
}

@Composable
private fun LogItem(log: Logger.LogEntry) {
    val color = when (log.level) {
        Logger.LogLevel.INFO -> Color(0xFFC7C7C7)
        Logger.LogLevel.WARN -> Color(0xFFFFD600)
        Logger.LogLevel.ERROR -> Color(0xFFFF5252)
        Logger.LogLevel.SUCCESS -> Color(0xFF69F0AE)
    }
    Text(
        text = "[${log.timestamp}] ${log.message}",
        color = color,
        fontSize = 12.sp,
        fontFamily = FontFamily.Monospace,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}
