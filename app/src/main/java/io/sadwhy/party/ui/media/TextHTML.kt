package io.sadwhy.party.ui.media

import android.content.Intent
import android.net.Uri
import android.text.Spanned
import android.text.style.URLSpan
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat

@Composable
fun ExpandableHtmlText(
    htmlText: String,
    minimizedMaxLines: Int = 3,
    linkColor: Color = MaterialTheme.colorScheme.primary
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Convert HTML to Spanned with proper formatting
    val spanned: Spanned = remember(htmlText) {
        HtmlCompat.fromHtml(
            htmlText, 
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
    }

    // Convert Spanned to AnnotatedString with links and formatting
    val annotatedString: AnnotatedString = remember(spanned) {
        buildAnnotatedString {
            val text = spanned.toString()
            append(text)
            
            // Extract and style links
            val spans = spanned.getSpans(0, spanned.length, URLSpan::class.java)
            spans.forEach { urlSpan ->
                val start = spanned.getSpanStart(urlSpan)
                val end = spanned.getSpanEnd(urlSpan)
                
                addStyle(
                    style = SpanStyle(
                        color = linkColor,
                        textDecoration = TextDecoration.Underline
                    ),
                    start = start,
                    end = end
                )
                
                addStringAnnotation(
                    tag = "URL",
                    annotation = urlSpan.url,
                    start = start,
                    end = end
                )
            }
        }
    }

    // Check if text needs expansion
    val needsExpansion = remember(annotatedString) {
        annotatedString.text.split('\n').size > minimizedMaxLines ||
        annotatedString.text.length > 200 // Adjust threshold as needed
    }

    Column {
        SelectionContainer {
            ClickableText(
                text = annotatedString,
                maxLines = if (expanded) Int.MAX_VALUE else minimizedMaxLines,
                overflow = TextOverflow.Ellipsis,
                onClick = { offset ->
                    // Handle link clicks
                    annotatedString.getStringAnnotations(
                        tag = "URL",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let { annotation ->
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Handle invalid URLs gracefully
                        }
                    }
                }
            )
        }
        
        if (needsExpansion) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (expanded) "Read less" else "Read more",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.clickable { expanded = !expanded }
            )
        }
    }
}

// better HTML parsing for complex content
@Composable
fun AdvancedHtmlText(
    htmlText: String,
    minimizedMaxLines: Int = 3,
    linkColor: Color = MaterialTheme.colorScheme.primary
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    // Parse HTML and create formatted text blocks
    val textBlocks = remember(htmlText) {
        parseHtmlToBlocks(htmlText)
    }
    
    Column {
        SelectionContainer {
            Column {
                textBlocks.forEachIndexed { index, block ->
                    when (block.type) {
                        BlockType.TEXT -> {
                            Text(
                                text = block.content,
                                maxLines = if (expanded || index < minimizedMaxLines) Int.MAX_VALUE else 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        BlockType.LINK -> {
                            Text(
                                text = block.content,
                                color = linkColor,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.clickable {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(block.url))
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        // Handle invalid URLs
                                    }
                                }
                            )
                        }
                        BlockType.BREAK -> {
                            if (expanded || index < minimizedMaxLines) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
        
        if (textBlocks.size > minimizedMaxLines) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (expanded) "Read less" else "Read more",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.clickable { expanded = !expanded }
            )
        }
    }
}

// Data classes for structured HTML parsing
data class TextBlock(
    val content: String,
    val type: BlockType,
    val url: String? = null
)

enum class BlockType {
    TEXT, LINK, BREAK
}

// Simple HTML parser for basic content
private fun parseHtmlToBlocks(html: String): List<TextBlock> {
    val blocks = mutableListOf<TextBlock>()
    
    // Basic regex patterns for parsing
    val linkPattern = Regex("""<a\s+href="([^"]+)"[^>]*>([^<]+)</a>""")
    val paragraphPattern = Regex("""<p>(.*?)</p>""", RegexOption.DOT_MATCHES_ALL)
    val brPattern = Regex("""<br\s*/?>""")
    
    val paragraphs = paragraphPattern.findAll(html)
    
    paragraphs.forEach { paragraphMatch ->
        val content = paragraphMatch.groupValues[1]
        
        if (content.trim().isEmpty() || content == "<br>" || content == "<br/>") {
            blocks.add(TextBlock("", BlockType.BREAK))
            return@forEach
        }
        
        // Check for links within paragraph
        val links = linkPattern.findAll(content)
        if (links.any()) {
            var lastIndex = 0
            links.forEach { linkMatch ->
                // Add text before link
                val beforeLink = content.substring(lastIndex, linkMatch.range.first)
                if (beforeLink.isNotBlank()) {
                    blocks.add(TextBlock(beforeLink.trim(), BlockType.TEXT))
                }
                
                // Add link
                blocks.add(
                    TextBlock(
                        content = linkMatch.groupValues[2],
                        type = BlockType.LINK,
                        url = linkMatch.groupValues[1]
                    )
                )
                
                lastIndex = linkMatch.range.last + 1
            }
            
            // Add remaining text after last link
            val afterLinks = content.substring(lastIndex)
            if (afterLinks.isNotBlank()) {
                blocks.add(TextBlock(afterLinks.trim(), BlockType.TEXT))
            }
        } else {
            // No links, just add as text
            val cleanContent = content.replace(brPattern, "\n").trim()
            if (cleanContent.isNotBlank()) {
                blocks.add(TextBlock(cleanContent, BlockType.TEXT))
            }
        }
    }
    
    return blocks
}