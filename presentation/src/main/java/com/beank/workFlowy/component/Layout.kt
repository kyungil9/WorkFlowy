package com.beank.workFlowy.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun WeekLayout(
    modifier: Modifier = Modifier,
    topBar : @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHostState: SnackbarHostState,
    floatingActionButton : @Composable () -> Unit = {},
    content : @Composable (PaddingValues) -> Unit
){
    Scaffold (
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = remember{{SnackbarHost(hostState = snackbarHostState)}},
        floatingActionButton = floatingActionButton,
        modifier = modifier.fillMaxSize()
    ){
        content(it)
    }
}

@Composable
fun DefaultLayout(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { topBar() },
        bottomBar = { bottomBar() },
        modifier = modifier
    ) {
        content(it)
    }

}