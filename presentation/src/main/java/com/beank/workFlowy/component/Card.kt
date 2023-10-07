package com.beank.workFlowy.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingCard(
    title : String,
    comment : String? = null,
    height : Dp = 50.dp,
    color: Color = MaterialTheme.colorScheme.scrim,
    enable : Boolean = true,
    onClick : () -> Unit = {}
){
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(0f),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        enabled = enable
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall, color = color)
            HorizontalSpacer(width = 15.dp)
            comment?.let {
                Text(text = it, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.outline)
            }
        }

    }
}

@Composable
fun ToggleCard(
    title : String,
    checked : () -> Boolean,
    height : Dp = 40.dp,
    color: Color = MaterialTheme.colorScheme.scrim,
    onClick : (Boolean) -> Unit = {}
){
    Log.d("card","onclick:${onClick.hashCode()},title:${title.hashCode()},checked:${checked.hashCode()}")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(0f),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 5.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall, color = color)
            Switch(
                checked = checked(),
                onCheckedChange = onClick,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.outline,
                )
            )
        }

    }
}

@Composable
fun TextCard(
    title : String,
    comment : String? = null,
    height : Dp = 50.dp,
    color: Color = MaterialTheme.colorScheme.scrim,
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(0f),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall, color = color)
            HorizontalSpacer(width = 15.dp)
            comment?.let {
                Text(text = it, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.outline)
            }
        }

    }
}