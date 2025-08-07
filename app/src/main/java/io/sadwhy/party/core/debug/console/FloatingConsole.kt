package io.sadwhy.party.core.debug.console

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.sadwhy.party.core.debug.Logger
import io.sadwhy.party.R
import io.sadwhy.party.core.ui.components.clipToDeviceCornerRadius
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * A wrapper that displays a floating debug console over the provided content
 * Disabled in release.
 *
 * Usage:
 * FloatingConsole { MainScreen() }
 */
@Composable
fun FloatingConsole(content: @Composable () -> Unit) {
    // TODO: add `BuildConfig.DEBUG` when releasing
    if (true) {
        val consoleViewModel: FloatingConsoleViewModel = viewModel()

        Box(modifier = Modifier
            .fillMaxSize()
            .clipToDeviceCornerRadius()
        ) {
            content()
            FloatingConsoleView(viewModel = consoleViewModel)
        }
    } else {
        content()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun FloatingConsoleView(viewModel: FloatingConsoleViewModel) {
    val isExpanded by viewModel.isExpanded.collectAsState()
    val offset by viewModel.offset.collectAsState()

    Box(
        modifier = Modifier
            .offset { offset }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    viewModel.onDrag(
                        IntOffset(dragAmount.x.roundToInt(), dragAmount.y.roundToInt())
                    )
                }
            }
    ) {
        AnimatedContent(
            targetState = isExpanded,
            transitionSpec = {
                (fadeIn(tween(300)) + scaleIn(initialScale = 0.9f)) togetherWith
                        (fadeOut(tween(200)) + scaleOut(targetScale = 0.9f))
            },
            label = "FloatingConsoleAnimation"
        ) { expanded ->
            if (expanded) {
                val logs by viewModel.logs.collectAsState()
                ConsolePanel(
                    logs = logs,
                    onClose = { viewModel.toggleExpansion() }
                )
            } else {
                FloatingHeadIcon(onClick = { viewModel.toggleExpansion() })
            }
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
            painter = painterResource(R.drawable.terminal),
            contentDescription = "Open Console",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
internal fun ConsolePanel(logs: List<Logger.LogEntry>, onClose: () -> Unit) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(logs) {
        snapshotFlow { logs.size }
            .collect {
                if (logs.isNotEmpty()) {
                    coroutineScope.launch {
                        listState.animateScrollToItem(logs.size - 1)
                    }
                }
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
                IconButton(onClick = onClose, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close Console",
                        tint = Color.White
                    )
                }
                Text("Debug Console", color = Color.White)
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                items(logs) { log ->
                    LogItem(log, 12.sp)
                }
            }
        }
    }
}

@Composable
private fun LogItem(log: Logger.LogEntry, fontSize: TextUnit) {
    val color = when (log.level) {
        Logger.LogLevel.INFO -> Color(0xFFC7C7C7)
        Logger.LogLevel.WARN -> Color(0xFFFFD600)
        Logger.LogLevel.ERROR -> Color(0xFFFF5252)
        Logger.LogLevel.SUCCESS -> Color(0xFF69F0AE)
    }

    SelectionContainer {
        Text(
            text = "[${log.timestamp}] ${log.message}",
            color = color,
            fontSize = fontSize,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(vertical = 2.dp)
        )
    }
}
