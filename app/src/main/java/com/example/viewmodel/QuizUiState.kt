package com.example.viewmodel

import com.example.model.Difficulty
import com.example.model.DifficultyFilter
import com.example.model.Question
import com.example.model.QuestionResult

enum class ScreenState {
    HOME,
    LEARN,
    QUIZ,
    RESULT,
    REVIEW
}

data class DifficultyBreakdown(
    val difficulty: Difficulty,
    val correctCount: Int,
    val totalCount: Int
) {
    val percentage: Int
        get() = if (totalCount > 0) ((correctCount.toFloat() / totalCount) * 100).toInt() else 0
}

data class QuizUiState(
    val currentScreen: ScreenState = ScreenState.HOME,
    val currentLessonIndex: Int = 0,
    val selectedFilter: DifficultyFilter = DifficultyFilter.MIXED,
    val sessionQuestions: List<Question> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOption: Int? = null,
    val isSubmitted: Boolean = false,
    val answeredResults: List<QuestionResult> = emptyList(),
    val isExplanationSheetOpen: Boolean = false,
    val totalSessionsCompleted: Int = 0,
    val bestScore: Int = 0
) {
    val currentQuestion: Question?
        get() = sessionQuestions.getOrNull(currentIndex)

    val currentScore: Int
        get() = answeredResults.count { it.isCorrect }

    val totalQuestions: Int
        get() = sessionQuestions.size

    val isLastQuestion: Boolean
        get() = currentIndex == totalQuestions - 1

    val difficultyBreakdowns: List<DifficultyBreakdown>
        get() {
            return Difficulty.entries.map { diff ->
                val diffQuestions = answeredResults.filter { it.question.difficulty == diff }
                val correct = diffQuestions.count { it.isCorrect }
                DifficultyBreakdown(diff, correct, diffQuestions.size)
            }.filter { it.totalCount > 0 }
        }
}
