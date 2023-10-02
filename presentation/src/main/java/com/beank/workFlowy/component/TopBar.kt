package com.beank.workFlowy.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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