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
