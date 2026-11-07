package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern

// Code Editor Dark Palette (Catppuccin Mocha inspired)
private val EditorBg = Color(0xFF181825)
private val EditorHeaderBg = Color(0xFF1E1E2E)
private val EditorBorder = Color(0xFF313244)
private val LineNumberColor = Color(0xFF6C7086)
private val CodeTextColor = Color(0xFFCDD6F4)

// Syntax highlighting colors
private val KeywordColor = Color(0xFFCBA6F7)   // Mauve / Light Purple
private val FunctionColor = Color(0xFF89B4FA)  // Blue / Sky
private val StringColor = Color(0xFFA6E3A1)    // Green
private val NumberColor = Color(0xFFFAB387)    // Peach
private val CommentColor = Color(0xFF7F849C)   // Gray italic
private val OperatorColor = Color(0xFF89DCEB)  // Cyan

@Composable
fun CodeEditorBox(
    sqlCode: String,
    modifier: Modifier = Modifier,
    fileName: String = "query.sql",
    showLineNumbers: Boolean = true
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var copied by remember { mutableStateOf(false) }

    val lines = remember(sqlCode) { sqlCode.lines() }
    val highlightedCode = remember(sqlCode) { highlightSql(sqlCode) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(EditorBg)
            .border(1.dp, EditorBorder, RoundedCornerShape(16.dp))
            .testTag("code_editor_box")
    ) {
        // Window Title Bar / Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(EditorHeaderBg)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Window control dots
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF38BA8)) // Red
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF9E2AF)) // Yellow
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFA6E3A1)) // Green
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = fileName,
                    color = LineNumberColor,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium
                )
            }

            // Copy Action Button
            IconButton(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("SQL Snippet", sqlCode)
                    clipboard.setPrimaryClip(clip)
                    copied = true
                    Toast.makeText(context, "SQL copied to clipboard", Toast.LENGTH_SHORT).show()
                    coroutineScope.launch {
                        delay(2000)
                        copied = false
                    }
                },
                modifier = Modifier
                    .size(28.dp)
                    .testTag("copy_sql_button")
            ) {
                if (copied) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Copied",
                        tint = Color(0xFFA6E3A1),
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy SQL",
                        tint = LineNumberColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Code Body with Line Numbers & Code
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            if (showLineNumbers) {
                // Line numbers column
                Column(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 10.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    lines.indices.forEach { index ->
                        Text(
                            text = (index + 1).toString(),
                            color = LineNumberColor,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            lineHeight = 20.sp
                        )
                    }
                }

                // Vertical Divider Line
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height((lines.size * 20).dp)
                        .background(EditorBorder)
                )
            }

            // Syntax-highlighted code block
            Box(
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Text(
                    text = highlightedCode,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

/**
 * Parses and syntax-highlights SQL statements into an AnnotatedString.
 */
fun highlightSql(sql: String): AnnotatedString {
    val builder = AnnotatedString.Builder(sql)
    // Default color for entire text
    builder.addStyle(
        SpanStyle(color = CodeTextColor),
        0,
        sql.length
    )

    // Regex definitions
    val keywordPattern = Pattern.compile(
        "\\b(SELECT|FROM|WHERE|GROUP\\s+BY|HAVING|ORDER\\s+BY|LIMIT|OFFSET|" +
                "JOIN|INNER\\s+JOIN|LEFT\\s+JOIN|RIGHT\\s+JOIN|FULL\\s+JOIN|CROSS\\s+JOIN|" +
                "ON|AS|AND|OR|NOT|IN|BETWEEN|LIKE|IS|NULL|CASE|WHEN|THEN|ELSE|END|" +
                "WITH|RECURSIVE|OVER|PARTITION\\s+BY|UNION|UNION\\s+ALL|DISTINCT|" +
                "ASC|DESC|ROWS|RANGE|PRECEDING|FOLLOWING|UNBOUNDED|CURRENT\\s+ROW|" +
                "INSERT|INTO|VALUES|UPDATE|SET|DELETE)\\b",
        Pattern.CASE_INSENSITIVE
    )

    val functionPattern = Pattern.compile(
        "\\b(COUNT|SUM|AVG|MIN|MAX|ROW_NUMBER|RANK|DENSE_RANK|LEAD|LAG|" +
                "COALESCE|CONCAT|SUBSTRING|UPPER|LOWER|ROUND|DATE|NOW|CAST)\\b",
        Pattern.CASE_INSENSITIVE
    )

    val stringPattern = Pattern.compile("'([^'\\\\]|\\\\.)*'")
    val numberPattern = Pattern.compile("\\b\\d+(\\.\\d+)?\\b")
    val commentPattern = Pattern.compile("--.*$|/\\*.*?\\*/", Pattern.MULTILINE)

    // Highlight comments first (comments can contain words that look like keywords)
    val commentMatcher = commentPattern.matcher(sql)
    while (commentMatcher.find()) {
        builder.addStyle(
            SpanStyle(color = CommentColor),
            commentMatcher.start(),
            commentMatcher.end()
        )
    }

    // Highlight strings
    val stringMatcher = stringPattern.matcher(sql)
    while (stringMatcher.find()) {
        builder.addStyle(
            SpanStyle(color = StringColor),
            stringMatcher.start(),
            stringMatcher.end()
        )
    }

    // Highlight numbers
    val numberMatcher = numberPattern.matcher(sql)
    while (numberMatcher.find()) {
        builder.addStyle(
            SpanStyle(color = NumberColor),
            numberMatcher.start(),
            numberMatcher.end()
        )
    }

    // Highlight functions
    val functionMatcher = functionPattern.matcher(sql)
    while (functionMatcher.find()) {
        builder.addStyle(
            SpanStyle(color = FunctionColor, fontWeight = FontWeight.SemiBold),
            functionMatcher.start(),
            functionMatcher.end()
        )
    }

    // Highlight keywords
    val keywordMatcher = keywordPattern.matcher(sql)
    while (keywordMatcher.find()) {
        builder.addStyle(
            SpanStyle(color = KeywordColor, fontWeight = FontWeight.Bold),
            keywordMatcher.start(),
            keywordMatcher.end()
        )
    }

    return builder.toAnnotatedString()
}
