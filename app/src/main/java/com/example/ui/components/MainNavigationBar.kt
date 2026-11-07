package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import com.example.viewmodel.ScreenState

private data class MainDestination(
    val screen: ScreenState,
    val label: String,
    val icon: ImageVector,
    val tag: String
)

private val mainDestinations = listOf(
    MainDestination(ScreenState.HOME, "Latihan", Icons.Default.Terminal, "nav_practice"),
    MainDestination(ScreenState.LEARN, "Basic SQL", Icons.Default.School, "nav_basic_sql"),
    MainDestination(ScreenState.JOINS, "JOIN", Icons.Default.Link, "nav_joins")
)

@Composable
fun MainNavigationBar(
    currentScreen: ScreenState,
    onSelectSection: (ScreenState) -> Unit
) {
    NavigationBar(modifier = Modifier.testTag("main_navigation")) {
        mainDestinations.forEach { destination ->
            NavigationBarItem(
                selected = currentScreen == destination.screen,
                onClick = { onSelectSection(destination.screen) },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = null
                    )
                },
                label = { Text(destination.label) },
                modifier = Modifier.testTag(destination.tag)
            )
        }
    }
}
