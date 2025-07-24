package io.sadwhy.party.ui.media

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.core.text.HtmlCompat

/**
 * A composable that displays HTML content, supports link clicks, and includes an
 * expandable "Read more/Read less" toggle.
 *
 * @param htmlText The HTML string to display.
 * @param minimizedMaxLines The number of lines to show when the text is collapsed.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun ExpandableHtmlText(
    htmlText: String,
    minimizedMaxLines: Int = 4,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isOverflowing by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val annotatedString = remember(htmlText) {
        val spanned = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT)
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)
            ) {
                append(spanned)
            }

            // Find all URL spans and apply a custom style
            val urlSpans = spanned.getSpans(0, spanned.length, android.text.style.URLSpan::class.java)
            urlSpans.forEach { span ->
                val start = spanned.getSpanStart(span)
                val end = spanned.getSpanEnd(span)
                addStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary, // Use theme's primary color for links
                        textDecoration = TextDecoration.Underline
                    ),
                    start = start,
                    end = end
                )
                addStringAnnotation(
                    tag = "URL",
                    annotation = span.url,
                    start = start,
                    end = end
                )
            }
        }
    }

    Column(
        modifier = modifier
            .animateContentSize() // animates expanding/collapsing
    ) {
        // ClickableText is used to handle both link clicks and expansion toggling
        ClickableText(
            text = annotatedString,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = if (isExpanded) Int.MAX_VALUE else minimizedMaxLines,
            onTextLayout = { textLayoutResult ->
                isOverflowing = textLayoutResult.hasVisualOverflow
            },
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                    .firstOrNull()
                    ?.let { annotation ->
                        // open it in a browser for now
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                        context.startActivity(intent)
                    }
                    ?: run {
                        // toggle the expanded state if the text is overflowing
                        if (isOverflowing || isExpanded) {
                           isExpanded = !isExpanded
                        }
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
