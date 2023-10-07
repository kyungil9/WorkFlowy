package com.beank.workFlowy.component

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.beank.domain.model.Tag
import com.beank.workFlowy.navigation.NavigationItem

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
    visible : () -> Boolean,
    tagList: () -> List<Tag>,
    onAddActTag : () -> Unit,
    onClickAct : (Tag) -> Unit,
    onClickDelect : (Tag) -> Unit,
    onDismissRequest: () -> Unit
){
    AnimatedVisibility(
        visible = visible()
    ) {
        CustomAlertDialog(
            onDismissRequest = { onDismissRequest() },
            properties = DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true)
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(space = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
                contentPadding = PaddingValues(all = 10.dp)
            ) {
                items(tagList(), key = {it.title}) { tag ->
                    ActTagCard(tag = tag, onClickAct = onClickAct, onClickDelect = onClickDelect)
                }
                item {
                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable { onAddActTag() },
                        shape = MaterialTheme.shapes.small,
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.shapes.small)
                                .height(100.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = NavigationItem.TAG.icon!!),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(20.dp),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}