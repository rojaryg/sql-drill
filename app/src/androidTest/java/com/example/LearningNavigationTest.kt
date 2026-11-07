package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.QuizViewModel
import com.example.viewmodel.ScreenState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LearningNavigationTest {
    @get:Rule
    val composeRule = createComposeRule()

    private fun launchApp(): QuizViewModel {
        val viewModel = QuizViewModel()
        composeRule.setContent {
            MyApplicationTheme(dynamicColor = false) {
                SqlDrillApp(quizViewModel = viewModel)
            }
        }
        return viewModel
    }

    @Test
    fun bottomIcons_openSeparateLearningPages() {
        launchApp()
        composeRule.onNodeWithTag("nav_practice").assertIsSelected()
        composeRule.onNodeWithTag("nav_basic_sql").performClick()
        composeRule.onNodeWithTag("basic_sql_learning_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("nav_basic_sql").assertIsSelected()

        composeRule.onNodeWithTag("nav_joins").performClick()
        composeRule.onNodeWithTag("joins_learning_screen").assertIsDisplayed()
        composeRule.onNodeWithText("Kenali hubungan antartabel").assertIsDisplayed()
        composeRule.onNodeWithTag("basic_sql_learning_screen").assertDoesNotExist()
        composeRule.onNodeWithTag("nav_joins").assertIsSelected()

        composeRule.onNodeWithTag("nav_practice").performClick()
        composeRule.onNodeWithTag("start_quiz_button").assertIsDisplayed()
        composeRule.onNodeWithTag("joins_learning_screen").assertDoesNotExist()
    }

    @Test
    fun switchingTabs_resumesEachLesson() {
        launchApp()
        composeRule.onNodeWithTag("nav_basic_sql").performClick()
        repeat(2) {
            composeRule.onNodeWithTag("next_lesson_button").performClick()
        }
        composeRule.onNodeWithText("Saring baris sebelum ditampilkan").assertIsDisplayed()

        composeRule.onNodeWithTag("nav_joins").performClick()
        composeRule.onNodeWithTag("next_lesson_button").performClick()
        composeRule.onNodeWithText("Ambil baris yang memiliki pasangan").assertIsDisplayed()

        composeRule.onNodeWithTag("nav_basic_sql").performClick()
        composeRule.onNodeWithText("Saring baris sebelum ditampilkan").assertIsDisplayed()
        composeRule.onNodeWithTag("nav_joins").performClick()
        composeRule.onNodeWithText("Ambil baris yang memiliki pasangan").assertIsDisplayed()
    }

    @Test
    fun startingQuiz_hidesBottomNavigation() {
        val viewModel = launchApp()
        composeRule.onNodeWithTag("start_quiz_button").performClick()
        composeRule.onNodeWithTag("main_navigation").assertDoesNotExist()
        composeRule.runOnIdle {
            assertEquals(ScreenState.QUIZ, viewModel.uiState.value.currentScreen)
        }
    }
}
