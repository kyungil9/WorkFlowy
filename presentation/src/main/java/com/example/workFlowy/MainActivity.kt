package com.example.workFlowy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.workFlowy.component.WeekAppBar
import com.example.workFlowy.component.WeekLayout
import com.example.workFlowy.component.WeekLazyList
import com.example.workFlowy.ui.theme.WorkFlowyTheme
import com.example.workFlowy.utils.today
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val weekViewModel : WeekViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkFlowyTheme {
                // A surface container using the 'background' color from the theme
                WeekLayout(
                    scaffoldState = rememberScaffoldState(),
                    topBar = {
                        WeekAppBar(
                            headerIcon = R.drawable.baseline_dehaze_24,
                            modifier = Modifier,
                            weekViewModel = weekViewModel,
                            tailIcon = R.drawable.baseline_dehaze_24)},
                    weekViewModel = weekViewModel
                ) {

                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WorkFlowyTheme {

    }
}