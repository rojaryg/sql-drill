package com.example

import com.example.data.SqlJoinLessonRepository
import com.example.data.SqlQuestionRepository
import com.example.model.Difficulty
import com.example.model.DifficultyFilter
import com.example.ui.components.highlightSql
import com.example.viewmodel.QuizViewModel
import com.example.viewmodel.ScreenState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SqlDrillUnitTest {

    @Test
    fun repository_returnsExactlyTenQuestionsPerSession() {
        val basicQuestions = SqlQuestionRepository.getQuestionsByFilter(DifficultyFilter.BASIC)
        val intermediateQuestions = SqlQuestionRepository.getQuestionsByFilter(DifficultyFilter.INTERMEDIATE)
        val advancedQuestions = SqlQuestionRepository.getQuestionsByFilter(DifficultyFilter.ADVANCED)
        val mixedQuestions = SqlQuestionRepository.getQuestionsByFilter(DifficultyFilter.MIXED)

        assertEquals("Basic session must have 10 questions", 10, basicQuestions.size)
        assertEquals("Intermediate session must have 10 questions", 10, intermediateQuestions.size)
        assertEquals("Advanced session must have 10 questions", 10, advancedQuestions.size)
        assertEquals("Mixed session must have 10 questions", 10, mixedQuestions.size)
    }

    @Test
    fun repository_hasValidQuestionsStructure() {
        val allQuestions = SqlQuestionRepository.getQuestionsByFilter(DifficultyFilter.MIXED)
        for (q in allQuestions) {
            assertTrue("Question title must not be blank", q.title.isNotBlank())
            assertTrue("SQL snippet must not be blank", q.sqlSnippet.isNotBlank())
            assertEquals("Must have 4 options", 4, q.options.size)
            assertTrue("Correct option index must be in range 0..3", q.correctOptionIndex in 0..3)
            assertTrue("Explanation must not be blank", q.explanation.isNotBlank())
        }
    }

    @Test
    fun viewModel_sessionFlowAndScoring() {
        val viewModel = QuizViewModel()
        assertEquals(ScreenState.HOME, viewModel.uiState.value.currentScreen)

        viewModel.selectDifficultyFilter(DifficultyFilter.BASIC)
        assertEquals(DifficultyFilter.BASIC, viewModel.uiState.value.selectedFilter)

        viewModel.startQuizSession()
        assertEquals(ScreenState.QUIZ, viewModel.uiState.value.currentScreen)
        assertEquals(10, viewModel.uiState.value.sessionQuestions.size)
        assertEquals(0, viewModel.uiState.value.currentIndex)
        assertFalse(viewModel.uiState.value.isSubmitted)

        val currentQ = viewModel.uiState.value.currentQuestion
        assertNotNull(currentQ)

        // Select correct option
        viewModel.selectOption(currentQ!!.correctOptionIndex)
        assertEquals(currentQ.correctOptionIndex, viewModel.uiState.value.selectedOption)

        // Submit answer
        viewModel.submitAnswer()
        assertTrue(viewModel.uiState.value.isSubmitted)
        assertEquals(1, viewModel.uiState.value.answeredResults.size)
        assertTrue(viewModel.uiState.value.answeredResults.first().isCorrect)
        assertEquals(1, viewModel.uiState.value.currentScore)

        // Move to next question
        viewModel.nextQuestion()
        assertEquals(1, viewModel.uiState.value.currentIndex)
        assertFalse(viewModel.uiState.value.isSubmitted)
    }

    @Test
    fun syntaxHighlighter_annotatesSqlKeywords() {
        val sampleSql = "SELECT name, age FROM users WHERE age > 21;"
        val annotated = highlightSql(sampleSql)
        assertEquals(sampleSql, annotated.text)
        assertTrue("Highlighted text must contain span styles", annotated.spanStyles.isNotEmpty())
    }

    @Test
    fun learning_navigationPreservesQuizStatisticsAndDifficulty() {
        val viewModel = QuizViewModel()
        viewModel.selectDifficultyFilter(DifficultyFilter.ADVANCED)
        viewModel.startQuizSession()
        repeat(10) {
            val question = viewModel.uiState.value.currentQuestion!!
            viewModel.selectOption(question.correctOptionIndex)
            viewModel.submitAnswer()
            viewModel.nextQuestion()
        }
        viewModel.returnToHome()

        viewModel.navigateToSection(ScreenState.LEARN)
        assertEquals(ScreenState.LEARN, viewModel.uiState.value.currentScreen)
        assertEquals(0, viewModel.uiState.value.currentLessonIndex)
        viewModel.selectLesson(4)
        assertEquals(4, viewModel.uiState.value.currentLessonIndex)
        viewModel.selectLesson(-1)
        viewModel.selectLesson(5)
        assertEquals(4, viewModel.uiState.value.currentLessonIndex)

        viewModel.returnToHome()
        assertEquals(ScreenState.HOME, viewModel.uiState.value.currentScreen)
        assertEquals(DifficultyFilter.ADVANCED, viewModel.uiState.value.selectedFilter)
        assertEquals(1, viewModel.uiState.value.totalSessionsCompleted)
        assertEquals(10, viewModel.uiState.value.bestScore)

        viewModel.navigateToSection(ScreenState.LEARN)
        assertEquals(4, viewModel.uiState.value.currentLessonIndex)
    }

    @Test
    fun learning_practiceStartsFreshBasicQuiz() {
        val viewModel = QuizViewModel()
        viewModel.selectDifficultyFilter(DifficultyFilter.ADVANCED)
        viewModel.navigateToSection(ScreenState.LEARN)
        viewModel.selectLesson(4)
        viewModel.startBasicSqlPractice()

        val state = viewModel.uiState.value
        assertEquals(ScreenState.QUIZ, state.currentScreen)
        assertEquals(DifficultyFilter.BASIC, state.selectedFilter)
        assertEquals(10, state.totalQuestions)
        assertTrue(state.sessionQuestions.all { it.difficulty == Difficulty.BASIC })
        assertEquals(0, state.currentIndex)
        assertEquals(0, state.currentScore)
        assertTrue(state.answeredResults.isEmpty())
        assertFalse(state.isSubmitted)
    }

    @Test
    fun bottomNavigation_preservesEachCoursesPosition() {
        val viewModel = QuizViewModel()
        viewModel.selectDifficultyFilter(DifficultyFilter.ADVANCED)
        viewModel.navigateToSection(ScreenState.LEARN)
        viewModel.selectLesson(4)
        viewModel.navigateToSection(ScreenState.JOINS)
        viewModel.selectJoinLesson(2)
        viewModel.selectJoinLesson(-1)
        viewModel.selectJoinLesson(SqlJoinLessonRepository.lessons.size)
        assertEquals(2, viewModel.uiState.value.currentJoinLessonIndex)

        viewModel.navigateToSection(ScreenState.HOME)
        viewModel.navigateToSection(ScreenState.LEARN)
        assertEquals(4, viewModel.uiState.value.currentLessonIndex)
        viewModel.navigateToSection(ScreenState.JOINS)
        assertEquals(ScreenState.JOINS, viewModel.uiState.value.currentScreen)
        assertEquals(2, viewModel.uiState.value.currentJoinLessonIndex)
        assertEquals(DifficultyFilter.ADVANCED, viewModel.uiState.value.selectedFilter)
        assertTrue(viewModel.uiState.value.sessionQuestions.isEmpty())
    }

    @Test
    fun bottomNavigation_cannotInterruptAnActiveQuiz() {
        val viewModel = QuizViewModel()
        viewModel.startQuizSession()
        val question = viewModel.uiState.value.currentQuestion!!
        viewModel.selectOption(question.correctOptionIndex)
        viewModel.submitAnswer()
        viewModel.navigateToSection(ScreenState.JOINS)
        viewModel.navigateToSection(ScreenState.RESULT)

        assertEquals(ScreenState.QUIZ, viewModel.uiState.value.currentScreen)
        assertEquals(1, viewModel.uiState.value.currentScore)
        assertTrue(viewModel.uiState.value.isSubmitted)
        assertFalse(ScreenState.QUIZ.isMainSection)
        assertFalse(ScreenState.RESULT.isMainSection)
        assertFalse(ScreenState.REVIEW.isMainSection)
    }

    @Test
    fun joinsPractice_startsIntermediateSession() {
        val viewModel = QuizViewModel()
        viewModel.navigateToSection(ScreenState.JOINS)
        viewModel.selectJoinLesson(SqlJoinLessonRepository.lessons.lastIndex)
        viewModel.startJoinPractice()

        val state = viewModel.uiState.value
        assertEquals(ScreenState.QUIZ, state.currentScreen)
        assertEquals(DifficultyFilter.INTERMEDIATE, state.selectedFilter)
        assertEquals(10, state.totalQuestions)
        assertTrue(state.sessionQuestions.all { it.difficulty == Difficulty.INTERMEDIATE })
        assertEquals(0, state.currentIndex)
        assertTrue(state.answeredResults.isEmpty())
    }
}
