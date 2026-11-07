package com.example.model

data class SqlLesson(
    val keyword: String,
    val title: String,
    val explanation: String,
    val sql: String,
    val resultColumns: List<String>,
    val resultRows: List<List<String>>,
    val resultExplanation: String,
    val tip: String
)

data class SqlExampleTable(
    val name: String,
    val columns: List<String>,
    val rows: List<List<String>>
)

data class SqlCourse(
    val id: String,
    val title: String,
    val description: String,
    val prerequisite: String,
    val sourceTables: List<SqlExampleTable>,
    val lessons: List<SqlLesson>,
    val combinedExample: SqlLesson,
    val recap: List<String>,
    val practiceFilter: DifficultyFilter
)
