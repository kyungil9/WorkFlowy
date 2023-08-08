package com.example.workFlowy.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.domain.model.Tag
import com.example.workFlowy.screen.Home.WeekUiState
import com.example.workFlowy.navigation.NavigationItem

@Composable
fun CustomAlertDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content : @Composable () -> Unit
){
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        content()
    }
}

@Composable
fun TagSelectedDialog(
    visible : Boolean,
    uiState: WeekUiState,
    onAddActTag : () -> Unit,
    onClickAct : (Tag) -> Unit,
    onDismissRequest: () -> Unit
){
    AnimatedVisibility(
        visible = visible
    ) {
        CustomAlertDialog(
            onDismissRequest = { onDismissRequest() },
            properties = DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(space = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
                contentPadding = PaddingValues(all = 10.dp)
            ) {
                items(uiState.tagList) { tag ->
                    ActTagCard(tag = tag, onClickAct = onClickAct)
                }
                item {
                    Card(
                        Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .clickable { onAddActTag },
                        shape = RoundedCornerShape(10.dp),
                        elevation = 5.dp
                    ) {
                        Image(
                            painter = painterResource(id = NavigationItem.TAG.icon!!),
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(20.dp)
                        )
                    }
                }
            }
        }
    }
}