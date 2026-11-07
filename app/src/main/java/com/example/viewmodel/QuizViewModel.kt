package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.SqlLessonRepository
import com.example.data.SqlQuestionRepository
import com.example.model.DifficultyFilter
import com.example.model.QuestionResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QuizViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    fun openBasicSqlLesson() {
        _uiState.update {
            it.copy(currentScreen = ScreenState.LEARN, currentLessonIndex = 0)
        }
    }

    fun selectLesson(index: Int) {
        if (index !in SqlLessonRepository.basicSqlLessons.indices) return
        _uiState.update { it.copy(currentLessonIndex = index) }
    }

    fun startBasicSqlPractice() {
        selectDifficultyFilter(DifficultyFilter.BASIC)
        startQuizSession()
    }

    fun selectDifficultyFilter(filter: DifficultyFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun startQuizSession() {
        val questions = SqlQuestionRepository.getQuestionsByFilter(_uiState.value.selectedFilter)
        _uiState.update {
            it.copy(
                currentScreen = ScreenState.QUIZ,
                sessionQuestions = questions,
                currentIndex = 0,
                selectedOption = null,
                isSubmitted = false,
                answeredResults = emptyList(),
                isExplanationSheetOpen = false
            )
        }
    }

    fun selectOption(optionIndex: Int) {
        if (_uiState.value.isSubmitted) return
        _uiState.update { it.copy(selectedOption = optionIndex) }
    }

    fun submitAnswer() {
        val state = _uiState.value
        val currentQ = state.currentQuestion ?: return
        val selected = state.selectedOption ?: return
        if (state.isSubmitted) return

        val isCorrect = selected == currentQ.correctOptionIndex
        val newResult = QuestionResult(
            question = currentQ,
            selectedOptionIndex = selected,
            isCorrect = isCorrect
        )

        _uiState.update {
            it.copy(
                isSubmitted = true,
                answeredResults = it.answeredResults + newResult
            )
        }
    }

    fun toggleExplanationSheet(isOpen: Boolean) {
        _uiState.update { it.copy(isExplanationSheetOpen = isOpen) }
    }

    fun nextQuestion() {
        val state = _uiState.value
        if (state.isLastQuestion) {
            // Finish session -> go to RESULT
            val currentScore = state.answeredResults.count { it.isCorrect }
            val newTotalCompleted = state.totalSessionsCompleted + 1
            val newBestScore = maxOf(state.bestScore, currentScore)

            _uiState.update {
                it.copy(
                    currentScreen = ScreenState.RESULT,
                    isExplanationSheetOpen = false,
                    totalSessionsCompleted = newTotalCompleted,
                    bestScore = newBestScore
                )
            }
        } else {
            // Next question
            _uiState.update {
                it.copy(
                    currentIndex = it.currentIndex + 1,
                    selectedOption = null,
                    isSubmitted = false,
                    isExplanationSheetOpen = false
                )
            }
        }
    }

    fun restartQuiz() {
        startQuizSession()
    }

    fun returnToHome() {
        _uiState.update {
            it.copy(
                currentScreen = ScreenState.HOME,
                sessionQuestions = emptyList(),
                currentIndex = 0,
                selectedOption = null,
                isSubmitted = false,
                answeredResults = emptyList(),
                isExplanationSheetOpen = false
            )
        }
    }

    fun openReview() {
        _uiState.update { it.copy(currentScreen = ScreenState.REVIEW) }
    }

    fun backToResults() {
        _uiState.update { it.copy(currentScreen = ScreenState.RESULT) }
    }
}
