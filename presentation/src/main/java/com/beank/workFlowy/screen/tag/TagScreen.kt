package com.beank.workFlowy.screen.tag

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.beank.presentation.R
import com.beank.workFlowy.component.BackTopBar
import com.beank.workFlowy.component.HorizontalSpacer
import com.beank.workFlowy.component.VerticalSpacer
import com.beank.workFlowy.component.WeekLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TagScreen(
    tagViewModel: TagViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onBackHome : () -> Unit
){
    val resources = LocalContext.current.resources
    val scope = rememberCoroutineScope()
    val uiState = tagViewModel.uiState

    LaunchedEffect(key1 = Unit){
        tagViewModel.initTagImages(resources.obtainTypedArray(R.array.tagList))
    }

    WeekLayout(
        snackbarHostState = snackbarHostState,
        topBar = remember{{
            BackTopBar(title = "태그 등록", onBack = onBackHome)
        }}
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalSpacer(height = 30.dp)
            Image(
                painter = painterResource(id = uiState.selectImage),
                modifier = Modifier.size(180.dp),
                contentDescription = null
            )
            VerticalSpacer(height = 20.dp)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                HorizontalSpacer(width = 10.dp)
                TextField(
                    value = uiState.title,
                    onValueChange = tagViewModel::updateSelectTagText,
                    modifier = Modifier.weight(0.5f)
                )
                HorizontalSpacer(width = 10.dp)
                Button(
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            if (uiState.title.isEmpty())
                                snackbarHostState.showSnackbar("이름을 입력해주세요.")
                            else if (!tagViewModel.checkTagText())
                                snackbarHostState.showSnackbar("중복된 이름이 있습니다.")
                            else{
                                tagViewModel.insertTagInfo()
                                scope.launch(Dispatchers.Main) { onBackHome() }
                            }
                        }},
                    modifier = Modifier.weight(0.25f)
                ) {
                    Text(text = "태그 등록", fontSize = 18.sp)
                }
                HorizontalSpacer(width = 10.dp)
            }
            VerticalSpacer(height = 30.dp)
            LazyVerticalGrid(
                columns = GridCells.Adaptive(100.dp),
                verticalArrangement = Arrangement.spacedBy(space = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 5.dp),
                contentPadding = PaddingValues(all = 5.dp)
            ){
                items(uiState.tagImageList){image ->
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = "태그사진",
                        modifier = Modifier
                            .height(50.dp)
                            .clickable { tagViewModel.updateSelectTagImage(image) }
                    )
                }
            }
        }
    }
}