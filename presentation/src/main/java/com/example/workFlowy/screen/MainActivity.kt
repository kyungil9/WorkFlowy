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
    private val tagViewModel : TagViewModel by viewModels()
    private val scheduleViewModel : ScheduleViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tagViewModel.initTagImages(resources.obtainTypedArray(R.array.tagList))
        scheduleViewModel.initScheduleImages(resources.obtainTypedArray(R.array.scheduleList))

        setContent {
            val navController = rememberNavController()
            weekViewModel.timer.schedule(weekViewModel.timerTask,0,1000)

            WorkFlowyTheme {
                NavigationGraph(
                    navController = navController,
                    weekViewModel = weekViewModel,
                    tagViewModel = tagViewModel,
                    scheduleViewModel = scheduleViewModel
                )
            }
        }
    }
}

