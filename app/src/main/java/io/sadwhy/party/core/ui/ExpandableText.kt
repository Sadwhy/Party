package io.sadwhy.party.core.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.jeziellago.compose.markdowntext.MarkdownText

/**
 * A composable that displays HTML/Markdown content using the MarkdownText library,
 * supports link clicks, text selection, and includes an expandable "Read more/Read less" toggle.
 *
 * @param htmlText The HTML or Markdown string to display.
 * @param minimizedMaxLines The number of lines to show when the text is collapsed.
 * @param modifier The modifier to be applied to the layout.
 * @param onLinkClick A callback that is invoked when a link in the text is clicked.
 * It receives the URL string.
 */
@Composable
fun ExpandableText(
    htmlText: String,
    minimizedMaxLines: Int = 4,
    modifier: Modifier = Modifier,
    onLinkClick: (url: String) -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isOverflowing by remember { mutableStateOf(false) }

    Column(modifier = modifier.animateContentSize()) {
        MarkdownText(
            markdown = htmlText,
            maxLines = if (isExpanded) Int.MAX_VALUE else minimizedMaxLines,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            linkColor = MaterialTheme.colorScheme.primary,
            isTextSelectable = true,
            onLinkClicked = { url ->
                onLinkClick(url)
            },
            onClick = {
                if (isOverflowing || isExpanded) {
                    isExpanded = !isExpanded
                }
            },
            onTextLayout = { numLines ->
                if (!isExpanded && !isOverflowing && numLines > minimizedMaxLines) {
                    isOverflowing = true
                }
            }
        )

        if (isOverflowing || isExpanded) {
            Text(
                text = if (isExpanded) "Read less" else "Read more",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.clickable { isExpanded = !isExpanded }
            )
        }
    }
}
