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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.beank.presentation.R


@Composable
fun WeekAppBar(
    @DrawableRes headerIcon : Int,
    modifier: Modifier = Modifier,
    onHeaderIconClick: () -> Unit = {},
    selectDay : () -> String,
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
        TextButton(onClick = onContentClick) {
            Text(
                text = selectDay(),
                style = MaterialTheme.typography.headlineSmall
            )
        }
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
            .height(60.dp)
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

@Composable
fun BackTopBar(
    title : String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(vertical = 10.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onBack
        ) {
            Icon(
                painter = painterResource(id = com.google.android.material.R.drawable.ic_arrow_back_black_24),
                contentDescription = "뒤로가기",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(30.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        IconButton(
            onClick = {},
            enabled = false
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_check_24),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier.size(30.dp)
            )
        }

    }
}