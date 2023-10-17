package com.beank.workFlowy.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DissmissBackground(
    dismissState: () -> DismissState
){
    val color by remember{
        derivedStateOf { when(dismissState().dismissDirection){
            DismissDirection.StartToEnd -> Color.Red
            DismissDirection.EndToStart -> Color.Green
            null -> Color.Transparent
        }}
    }
    val direction by remember{
        derivedStateOf { dismissState().dismissDirection == DismissDirection.StartToEnd }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        colors = CardDefaults.cardColors(color),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            if (direction)
                Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
            HorizontalSpacer(width = 1.dp)
            if (!direction)
                Icon(imageVector = Icons.Default.Edit, contentDescription = "update")
        }
    }

}