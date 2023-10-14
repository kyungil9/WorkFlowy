package com.beank.workFlowy.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DissmissBackground(
    dismissState: DismissState
){
    val direction = dismissState.dismissDirection
    val color = when(direction){
        DismissDirection.StartToEnd -> Color.Red
        DismissDirection.EndToStart -> Color.Green
        null -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(color = color)
            }
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        if (direction == DismissDirection.StartToEnd)
            Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
        HorizontalSpacer(width = 1.dp)
        if (direction == DismissDirection.EndToStart)
            Icon(imageVector = Icons.Default.Edit, contentDescription = "update")
    }
}