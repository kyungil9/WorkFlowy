package com.example.workFlowy.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.workFlowy.screen.Home.WeekViewModel
import com.example.workFlowy.navigation.NavigationGraph
import com.example.workFlowy.ui.theme.WorkFlowyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val weekViewModel : WeekViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            weekViewModel.timer.schedule(weekViewModel.timerTask,0,1000)

            WorkFlowyTheme {
                NavigationGraph(navController = navController, weekViewModel = weekViewModel)
            }
        }
    }
}

