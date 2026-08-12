package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.db.AppDatabase
import com.example.data.repository.ParliamentRepository
import com.example.ui.components.ParliamentBottomNavigation
import com.example.ui.components.ParliamentTopAppBar
import com.example.ui.screens.CommitteesScreen
import com.example.ui.screens.GalleryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JoinScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.ResearchScreen
import com.example.ui.theme.SinaiParliamentTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.ParliamentViewModel
import com.example.ui.viewmodel.ParliamentViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = ParliamentRepository(database.parliamentDao())
        val factory = ParliamentViewModelFactory(repository)

        setContent {
            SinaiParliamentTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    ParliamentMainContent(factory = factory)
                }
            }
        }
    }
}

@Composable
fun ParliamentMainContent(factory: ParliamentViewModelFactory) {
    val context = LocalContext.current
    val viewModel: ParliamentViewModel = viewModel(factory = factory)
    val currentScreen by viewModel.currentScreen.collectAsState()

    Scaffold(
        topBar = {
            ParliamentTopAppBar(
                currentScreen = currentScreen,
                onBackClick = if (currentScreen != AppScreen.HOME) {
                    { viewModel.navigateTo(AppScreen.HOME) }
                } else null,
                onJoinClick = {
                    viewModel.openOfficialGoogleForm(context)
                }
            )
        },
        bottomBar = {
            ParliamentBottomNavigation(
                currentScreen = currentScreen,
                onScreenSelected = { screen -> viewModel.navigateTo(screen) }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(
                    viewModel = viewModel,
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
                AppScreen.RESEARCH -> ResearchScreen(
                    viewModel = viewModel
                )
                AppScreen.COMMITTEES -> CommitteesScreen(
                    viewModel = viewModel,
                    onNavigateToJoin = { viewModel.navigateTo(AppScreen.JOIN) }
                )
                AppScreen.GALLERY -> GalleryScreen(
                    viewModel = viewModel
                )
                AppScreen.JOIN -> JoinScreen(
                    viewModel = viewModel
                )
                AppScreen.QUIZ -> QuizScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}
