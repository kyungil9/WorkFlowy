package com.beank.workFlowy.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeekLayout(
    modifier: Modifier = Modifier,
    topBar : @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHostState: SnackbarHostState,
    floatingActionButton : @Composable () -> Unit = {},
    content : @Composable (PaddingValues) -> Unit
){
    androidx.compose.material3.Scaffold (
        topBar = { topBar()},
        bottomBar = {bottomBar()},
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)},
        floatingActionButton = {floatingActionButton()},
        modifier = modifier
    ){
        content(it)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultLayout(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    androidx.compose.material3.Scaffold(
        topBar = { topBar() },
        bottomBar = { bottomBar() },
        modifier = modifier
    ) {
        content(it)
    }

}