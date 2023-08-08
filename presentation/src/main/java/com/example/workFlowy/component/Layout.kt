package com.example.workFlowy.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.workFlowy.R
import com.example.workFlowy.screen.Home.WeekViewModel
import com.example.workFlowy.ui.theme.white
import com.example.workFlowy.utils.transDayToKorean

@Composable
fun WeekLayout(
    modifier: Modifier = Modifier,
    topBar : @Composable () -> Unit = {},
    scaffoldState: ScaffoldState,
    content : @Composable (PaddingValues) -> Unit
){
    Scaffold (
        topBar = {topBar()},
        scaffoldState = scaffoldState,
        snackbarHost = {snackbarHostState ->
            Column() {
                SnackBarHostCustom(
                    headerMessage = snackbarHostState.currentSnackbarData?.message ?: "",
                    contentMessage = snackbarHostState.currentSnackbarData?.actionLabel ?: "",
                    snackBarHostState = scaffoldState.snackbarHostState,
                    disMissSnackBar = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() })

            }
        },
        backgroundColor = white,
        modifier = Modifier.fillMaxSize()
    ){
        content(it)
    }
}