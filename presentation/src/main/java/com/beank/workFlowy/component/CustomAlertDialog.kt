package com.beank.workFlowy.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.beank.domain.model.Tag
import com.beank.workFlowy.screen.home.WeekUiState
import com.beank.workFlowy.navigation.NavigationItem
import com.beank.workFlowy.ui.theme.white

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
    onClickDelect : (Tag) -> Unit,
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
                    ActTagCard(tag = tag, onClickAct = onClickAct, onClickDelect = onClickDelect)
                }
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .clickable { onAddActTag() },
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(white, shape = RoundedCornerShape(10.dp))
                                .height(100.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
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
}