package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.SqlJoinLessonRepository
import com.example.data.SqlLessonRepository
import com.example.ui.components.MainNavigationBar
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.screens.ReviewScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.QuizViewModel
import com.example.viewmodel.ScreenState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SqlDrillApp()
                }
            }
        }
    }
}

@Composable
fun SqlDrillApp(quizViewModel: QuizViewModel = viewModel()) {
    val uiState by quizViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (uiState.currentScreen.isMainSection) {
                MainNavigationBar(
                    currentScreen = uiState.currentScreen,
                    onSelectSection = quizViewModel::navigateToSection
                )
            }
        }
    ) { contentPadding ->
        AnimatedContent(
            targetState = uiState.currentScreen,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding),
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "ScreenTransition"
        ) { screen ->
            when (screen) {
                ScreenState.HOME -> {
                    HomeScreen(
                        uiState = uiState,
                        onSelectFilter = quizViewModel::selectDifficultyFilter,
                        onStartSession = quizViewModel::startQuizSession
                    )
                }
                ScreenState.LEARN -> {
                    LearnScreen(
                        course = SqlLessonRepository.course,
                        currentLessonIndex = uiState.currentLessonIndex,
                        onSelectLesson = quizViewModel::selectLesson,
                        onBack = quizViewModel::returnToHome,
                        onStartPractice = quizViewModel::startBasicSqlPractice
                    )
                }
                ScreenState.JOINS -> {
                    LearnScreen(
                        course = SqlJoinLessonRepository.course,
                        currentLessonIndex = uiState.currentJoinLessonIndex,
                        onSelectLesson = quizViewModel::selectJoinLesson,
                        onBack = quizViewModel::returnToHome,
                        onStartPractice = quizViewModel::startJoinPractice
                    )
                }
                ScreenState.QUIZ -> {
                    QuizScreen(
                        uiState = uiState,
                        onSelectOption = quizViewModel::selectOption,
                        onSubmitAnswer = quizViewModel::submitAnswer,
                        onNextQuestion = quizViewModel::nextQuestion,
                        onToggleExplanation = quizViewModel::toggleExplanationSheet,
                        onExitQuiz = quizViewModel::returnToHome
                    )
                }
                ScreenState.RESULT -> {
                    ResultScreen(
                        uiState = uiState,
                        onTryAgain = quizViewModel::restartQuiz,
                        onReturnHome = quizViewModel::returnToHome,
                        onOpenReview = quizViewModel::openReview
                    )
                }
                ScreenState.REVIEW -> {
                    ReviewScreen(
                        uiState = uiState,
                        onBackToResults = quizViewModel::backToResults
                    )
                }
            }
        }
    }
}
