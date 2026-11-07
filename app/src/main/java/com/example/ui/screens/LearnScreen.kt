package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.SqlCourse
import com.example.ui.components.CodeEditorBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnScreen(
    course: SqlCourse,
    currentLessonIndex: Int,
    onSelectLesson: (Int) -> Unit,
    onBack: () -> Unit,
    onStartPractice: () -> Unit
) {
    val lessons = course.lessons
    val lesson = lessons[currentLessonIndex]
    val isLastLesson = currentLessonIndex == lessons.lastIndex
    val contentState = rememberLazyListState()
    val topicsState = rememberLazyListState()

    BackHandler(onBack = onBack)
    LaunchedEffect(course.id, currentLessonIndex) {
        contentState.scrollToItem(0)
        topicsState.animateScrollToItem(currentLessonIndex)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().testTag("${course.id}_learning_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(course.title, fontWeight = FontWeight.Bold)
                        Text(
                            text = "Materi ${currentLessonIndex + 1} dari ${lessons.size}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("learning_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali ke beranda"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 4.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { onSelectLesson(currentLessonIndex - 1) },
                        enabled = currentLessonIndex > 0,
                        modifier = Modifier.weight(1f).testTag("previous_lesson_button")
                    ) {
                        Text("Sebelumnya")
                    }
                    Button(
                        onClick = {
                            if (isLastLesson) onStartPractice()
                            else onSelectLesson(currentLessonIndex + 1)
                        },
                        modifier = Modifier.weight(1f).testTag("next_lesson_button")
                    ) {
                        Text(if (isLastLesson) "Latihan ${course.practiceFilter.displayName}" else "Selanjutnya")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            LazyRow(
                state = topicsState,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(lessons, key = { _, item -> item.keyword }) { index, item ->
                    FilterChip(
                        selected = currentLessonIndex == index,
                        onClick = { onSelectLesson(index) },
                        label = { Text(item.keyword) },
                        modifier = Modifier.testTag("lesson_tab_${index}")
                    )
                }
            }
            LazyColumn(
                state = contentState,
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 20.dp,
                    vertical = 16.dp
                )
            ) {
                item(key = "introduction") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (currentLessonIndex == 0) {
                            Text(
                                text = course.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = course.prerequisite,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = lesson.keyword,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = lesson.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = lesson.explanation,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                course.sourceTables.forEach { table ->
                    item(key = "source_${table.name}") {
                        LessonSection(title = "Tabel contoh: ${table.name}") {
                            LessonDataTable(columns = table.columns, rows = table.rows)
                        }
                    }
                }
                item(key = "query") {
                    LessonSection(title = "Contoh query") {
                        CodeEditorBox(
                            sqlCode = lesson.sql,
                            fileName = "${course.id}_${currentLessonIndex + 1}.sql"
                        )
                    }
                }
                item(key = "result") {
                    LessonSection(title = "Hasil query") {
                        LessonDataTable(
                            columns = lesson.resultColumns,
                            rows = lesson.resultRows
                        )
                        Text(
                            text = lesson.resultExplanation,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                item(key = "tip") {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Ingat konsepnya",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = lesson.tip,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
                if (isLastLesson) {
                    item(key = "combined") {
                        val example = course.combinedExample
                        LessonSection(title = example.title) {
                            Text(
                                text = example.explanation,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            CodeEditorBox(
                                sqlCode = example.sql,
                                fileName = "${course.id}_summary.sql"
                            )
                            LessonDataTable(
                                columns = example.resultColumns,
                                rows = example.resultRows
                            )
                            Text(
                                text = example.resultExplanation,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = example.tip,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    item(key = "recap") {
                        LessonSection(title = "Bekal sebelum latihan") {
                            course.recap.forEach { point ->
                                Text(
                                    text = "• $point",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        content()
    }
}

@Composable
private fun LessonDataTable(columns: List<String>, rows: List<List<String>>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .width((columns.size * 140).dp)
                .padding(vertical = 4.dp)
        ) {
            Row {
                columns.forEach { column ->
                    Text(
                        text = column,
                        modifier = Modifier.width(140.dp).padding(12.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            rows.forEach { row ->
                HorizontalDivider()
                Row {
                    row.forEach { cell ->
                        Text(
                            text = cell,
                            modifier = Modifier.width(140.dp).padding(12.dp),
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
    if (columns.size > 2) {
        Text(
            text = "Geser tabel ke samping untuk melihat semua kolom.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
