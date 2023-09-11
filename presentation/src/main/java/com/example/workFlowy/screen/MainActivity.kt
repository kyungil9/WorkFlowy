package com.example.workFlowy.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.workFlowy.R
import com.example.workFlowy.screen.home.WeekViewModel
import com.example.workFlowy.navigation.NavigationGraph
import com.example.workFlowy.screen.schedule.ScheduleViewModel
import com.example.workFlowy.screen.tag.TagViewModel
import com.example.workFlowy.ui.theme.WorkFlowyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val weekViewModel : WeekViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weekViewModel.initTagImages(resources.obtainTypedArray(R.array.tagList))
        weekViewModel.initScheduleImages(resources.obtainTypedArray(R.array.scheduleList))
        weekViewModel.timerJob.start()

        setContent {
            val navController = rememberNavController()

            WorkFlowyTheme {
                NavigationGraph(
                    navController = navController,
                    weekViewModel = weekViewModel
                )
            }
        }
    }
}

