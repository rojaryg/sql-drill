package com.example.model

enum class Difficulty(val displayName: String, val description: String) {
    BASIC("Basic", "SELECT, WHERE, ORDER BY, Operators"),
    INTERMEDIATE("Intermediate", "JOINs, GROUP BY, HAVING, CASE WHEN"),
    ADVANCED("Advanced", "Window Functions, CTEs, Subqueries, Self JOINs")
}

enum class DifficultyFilter(val displayName: String, val subtitle: String) {
    BASIC("Basic", "Core fundamentals & single-table queries"),
    INTERMEDIATE("Intermediate", "Multi-table JOINs, grouping & conditionals"),
    ADVANCED("Advanced", "Window functions, CTEs & deep subqueries"),
    MIXED("Mixed", "Curated 10-question blend across all levels")
}

data class Question(
    val id: String,
    val title: String,
    val topic: String,
    val difficulty: Difficulty,
    val sqlSnippet: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String
)

data class QuestionResult(
    val question: Question,
    val selectedOptionIndex: Int,
    val isCorrect: Boolean
)
