package com.beank.workFlowy.screen.trigger

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.beank.domain.model.GeofenceData
import com.beank.domain.model.GeofenceEvent
import com.beank.presentation.R
import com.beank.workFlowy.component.BackTopBar
import com.beank.workFlowy.component.DissmissBackground
import com.beank.workFlowy.component.HorizontalSpacer
import com.beank.workFlowy.component.WeekLayout
import com.beank.workFlowy.navigation.NavigationItem
import com.beank.workFlowy.utils.intToImage
import com.beank.workFlowy.utils.toFormatString


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TriggerScreen(
    triggerViewModel: TriggerViewModel = hiltViewModel(),
    snackbarHostState : SnackbarHostState,
    onBack : () -> Unit,
    onMove : (String) -> Unit,
    onUpdate : (String,GeofenceData) -> Unit
) {
    val scrollState = rememberLazyListState()
    val uiState = triggerViewModel.uiState

    WeekLayout(
        snackbarHostState = snackbarHostState,
        topBar = remember{{
            BackTopBar(
                title = "트리거",
                onBack = onBack
            )
        }},
        floatingActionButton = remember{{
            FloatingActionButton(
                onClick = { onMove(NavigationItem.ADDTRIGGER.route) },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_24),
                    contentDescription = "트리거 추가",
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }}
    ) {
        if (uiState.progressToggle){
            CircularProgressIndicator()
        }else {
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding())
            ) {
                items(uiState.triggerList, key = { item -> item.hashCode() }) { geofenceData ->
                    TriggerItem(geofenceData = { geofenceData }, onRemove = triggerViewModel::onTriggerRemove, onUpdate = onUpdate)
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriggerItem(
    geofenceData: () -> GeofenceData,
    onRemove : (String) -> Unit,
    onUpdate : (String,GeofenceData) -> Unit
){
    var show by remember { mutableStateOf(true)}
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart || it == DismissValue.DismissedToEnd){
                show = false
                true
            } else
                false
        },
        positionalThreshold = {150.dp.value}
    )
    AnimatedVisibility(
        visible = show,
        exit = fadeOut(spring())
    ) {
        SwipeToDismiss(
            state = dismissState,
            background = {DissmissBackground(dismissState = dismissState)},
            dismissContent = { TriggerItem(geofenceData = geofenceData)})
    }
    LaunchedEffect(key1 = show){
        if (!show){
            when(dismissState.dismissDirection){
                DismissDirection.StartToEnd -> onRemove(geofenceData().id!!)
                DismissDirection.EndToStart -> onUpdate(NavigationItem.ADDTRIGGER.route,geofenceData())
                null -> Unit
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TriggerItem(geofenceData: () -> GeofenceData){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 15.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = intToImage(geofenceData().tagImage, LocalContext.current.resources.obtainTypedArray(R.array.tagList))),
            contentDescription = "트리거 태그 이미지",
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Column (
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)
        ){
            Row {
                Text(
                    text = when (geofenceData().geoEvent) {
                        GeofenceEvent.EnterRequest -> "Enter"
                        GeofenceEvent.ExitRequest -> "Exit"
                        GeofenceEvent.EnterOrExitRequest -> "Enter/Exit"
                        else -> ""
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                HorizontalSpacer(width = 10.dp)
                Text(
                    text = geofenceData().tag,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            if (geofenceData().timeOption){
                Text(text = "${geofenceData().startTime.toFormatString()}~${geofenceData().endTime.toFormatString()}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}