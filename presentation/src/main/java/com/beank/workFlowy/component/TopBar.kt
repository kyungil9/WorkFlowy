package com.beank.workFlowy.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun WeekAppBar(
    @DrawableRes headerIcon : Int,
    modifier: Modifier = Modifier,
    onHeaderIconClick: () -> Unit = {},
    selectDay : String,
    onContentClick: () -> Unit = {},
    @DrawableRes tailIcon : Int? = null,
    onTailIconClick: () -> Unit = {}
){
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onHeaderIconClick
        ) {
            Icon(
                modifier = Modifier
                    .clickable(
                        onClick = onHeaderIconClick,
                        interactionSource = remember { MutableInteractionSource()},
                        indication = rememberRipple(bounded = false, radius = 18.dp)),
                painter = painterResource(id = headerIcon),
                contentDescription = "menu"
            )
        }
        Text(
            text = selectDay,
            modifier = Modifier
                .clickable(onClick = onContentClick),
            fontSize = 18.sp
        )
        tailIcon?.let{
            IconButton(onClick = onTailIconClick) {
                Icon(
                    modifier = Modifier
                        .clickable(
                            onClick = onTailIconClick,
                            interactionSource = remember { MutableInteractionSource()},
                            indication = rememberRipple(bounded = false, radius = 18.dp)),
                    painter = painterResource(id = tailIcon),
                    contentDescription = "menu2"
                )
            }
        }
    }
}

@Composable
fun TextTopBar(
    title : String,
    modifier: Modifier = Modifier,
    onCancle: () -> Unit = {},
    onConfirm: () -> Unit = {}
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = 10.dp, horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.offset(x = 10.dp, y = 10.dp)
        )
        Row {
            TextButton(onClick = onCancle) {
                Text(text = "취소", fontSize = 16.sp, fontWeight = FontWeight.Light, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            TextButton(onClick = onConfirm) {
                Text(text = "완료", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    }
}