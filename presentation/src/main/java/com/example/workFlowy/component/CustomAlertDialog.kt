package com.example.workFlowy.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.datasource.local.database.entity.WeekTag
import com.example.domain.model.Tag
import com.example.workFlowy.R
import com.example.workFlowy.WeekViewModel
import java.util.Properties

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
    weekViewModel: WeekViewModel,
    onAddActTag : () -> Unit,
    onClickAct : (Tag) -> Unit,
    onDismissRequest: () -> Unit
){
//    val tags = ArrayList<WeekTag>()
//    tags.add(WeekTag(0, R.drawable.baseline_directions_run_24,"운동중"))
//    tags.add(WeekTag(0, R.drawable.baseline_directions_run_24,"운동중"))
//    tags.add(WeekTag(0, R.drawable.baseline_directions_run_24,"운동중"))
//    tags.add(WeekTag(0, R.drawable.baseline_directions_run_24,"운동중"))
//    tags.add(WeekTag(0, R.drawable.baseline_directions_run_24,"운동중"))
//    tags.add(WeekTag(0, R.drawable.baseline_directions_run_24,"운동중"))

    val uiState by weekViewModel.uiState.collectAsStateWithLifecycle()

    if (visible) {
        CustomAlertDialog(
            onDismissRequest = { onDismissRequest() },
            properties = DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(space = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
                contentPadding = PaddingValues(all = 10.dp)){
                items(uiState.tagList){tag ->
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
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.baseline_add_circle_24),
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