package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SqlQuestionRepository
import com.example.model.Difficulty
import com.example.model.DifficultyFilter
import com.example.viewmodel.QuizUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: QuizUiState,
    onSelectFilter: (DifficultyFilter) -> Unit,
    onStartSession: () -> Unit
) {
    var showCheatSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = onStartSession,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("start_quiz_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Start ${uiState.selectedFilter.displayName} Drill (10 Qs)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // App Bar / Top Identity
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF89B4FA), Color(0xFFCBA6F7))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Terminal,
                                contentDescription = "SQL Drill Logo",
                                tint = Color(0xFF181825),
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "SQL Drill",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Query Master & Interview Practice",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = { showCheatSheet = true },
                        modifier = Modifier.testTag("cheat_sheet_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                            contentDescription = "Quick SQL Guide",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Stats row
            item {
                StatsCard(
                    totalQuestions = SqlQuestionRepository.getTotalQuestionCount(),
                    sessionsCompleted = uiState.totalSessionsCompleted,
                    bestScore = uiState.bestScore
                )
            }

            // Section Header
            item {
                Column {
                    Text(
                        text = "Choose Difficulty",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Every session generates 10 targeted questions.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Difficulty Cards
            item {
                DifficultySelectorCard(
                    filter = DifficultyFilter.BASIC,
                    isSelected = uiState.selectedFilter == DifficultyFilter.BASIC,
                    icon = Icons.Default.School,
                    badgeColor = Color(0xFF10B981),
                    topics = listOf("SELECT", "WHERE", "ORDER BY", "NULLs"),
                    onClick = { onSelectFilter(DifficultyFilter.BASIC) }
                )
            }

            item {
                DifficultySelectorCard(
                    filter = DifficultyFilter.INTERMEDIATE,
                    isSelected = uiState.selectedFilter == DifficultyFilter.INTERMEDIATE,
                    icon = Icons.Default.FlashOn,
                    badgeColor = Color(0xFFF59E0B),
                    topics = listOf("JOINs", "GROUP BY", "HAVING", "CASE"),
                    onClick = { onSelectFilter(DifficultyFilter.INTERMEDIATE) }
                )
            }

            item {
                DifficultySelectorCard(
                    filter = DifficultyFilter.ADVANCED,
                    isSelected = uiState.selectedFilter == DifficultyFilter.ADVANCED,
                    icon = Icons.Default.Psychology,
                    badgeColor = Color(0xFF8B5CF6),
                    topics = listOf("Window Funcs", "CTEs", "Subqueries", "PARTITION"),
                    onClick = { onSelectFilter(DifficultyFilter.ADVANCED) }
                )
            }

            item {
                DifficultySelectorCard(
                    filter = DifficultyFilter.MIXED,
                    isSelected = uiState.selectedFilter == DifficultyFilter.MIXED,
                    icon = Icons.Default.DataObject,
                    badgeColor = MaterialTheme.colorScheme.primary,
                    topics = listOf("All Tiers", "Comprehensive Interview Prep"),
                    onClick = { onSelectFilter(DifficultyFilter.MIXED) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showCheatSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showCheatSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            SqlCheatSheetContent(onClose = { showCheatSheet = false })
        }
    }
}

@Composable
fun StatsCard(
    totalQuestions: Int,
    sessionsCompleted: Int,
    bestScore: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("stats_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(
                label = "Question Pool",
                value = "$totalQuestions",
                icon = Icons.Default.Storage,
                tint = MaterialTheme.colorScheme.primary
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(36.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
            )
            StatItem(
                label = "Completed",
                value = "$sessionsCompleted",
                icon = Icons.Default.CheckCircle,
                tint = Color(0xFF10B981)
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(36.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
            )
            StatItem(
                label = "Best Score",
                value = "$bestScore/10",
                icon = Icons.Default.EmojiEvents,
                tint = Color(0xFFF59E0B)
            )
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    icon: ImageVector,
    tint: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun DifficultySelectorCard(
    filter: DifficultyFilter,
    isSelected: Boolean,
    icon: ImageVector,
    badgeColor: Color,
    topics: List<String>,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    }

    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("difficulty_card_${filter.name.lowercase()}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(badgeColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = badgeColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = filter.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = filter.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Radio-like indicator
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                        )
                        .border(
                            2.dp,
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onPrimary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Topic Tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                topics.forEach { topic ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = topic,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SqlCheatSheetContent(onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SQL Execution Order Cheat Sheet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onClose) {
                Text("Close")
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = "SQL statements execute in a specific logical order, not top-to-bottom:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        val steps = listOf(
            "1. FROM & JOIN" to "Identify tables and cross join/on conditions",
            "2. WHERE" to "Filter base rows before aggregation",
            "3. GROUP BY" to "Bucket rows into distinct aggregated groups",
            "4. HAVING" to "Filter aggregated groups (can use COUNT, SUM)",
            "5. SELECT" to "Evaluate expressions, column aliases, & window functions",
            "6. DISTINCT" to "Eliminate duplicate tuples",
            "7. ORDER BY" to "Sort the output dataset",
            "8. LIMIT / OFFSET" to "Restrict row count"
        )

        steps.forEach { (order, desc) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = order,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.width(150.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
