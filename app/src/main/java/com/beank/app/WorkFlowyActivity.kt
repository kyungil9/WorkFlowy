package com.beank.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.beank.workFlowy.screen.WorkFlowyApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkFlowyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkFlowyApp()
        }
    }
}

