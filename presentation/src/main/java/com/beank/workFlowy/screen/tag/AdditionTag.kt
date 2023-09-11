package com.beank.workFlowy.screen.tag

import android.content.res.TypedArray
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.beank.workFlowy.component.HorizontalSpacer
import com.beank.workFlowy.component.VerticalSpacer
import com.beank.workFlowy.component.WeekLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagScreen(
    tagViewModel: TagViewModel = hiltViewModel(),
    tagImages : TypedArray,
    onBackHome : () -> Unit
){
    tagViewModel.initTagImages(tagImages)
    val selectTagImage by tagViewModel.selectTagImage.collectAsStateWithLifecycle()
    val selectTagText by tagViewModel.selectTagText.collectAsStateWithLifecycle()
    val uiState by tagViewModel.tagUiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var snackbarHostState = remember { SnackbarHostState() }



    WeekLayout(snackbarHostState = snackbarHostState) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalSpacer(height = 30.dp)
            Image(
                painter = painterResource(id = selectTagImage),
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
                    value = selectTagText,
                    onValueChange = {tagViewModel.updateSelectTagText(it)},
                    modifier = Modifier.weight(0.5f)
                )
                HorizontalSpacer(width = 10.dp)
                Button(
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            if (tagViewModel.selectTagText.value.isEmpty())
                                snackbarHostState.showSnackbar("이름을 입력해주세요.")
                            else if (tagViewModel.checkTagText() > 0)
                                snackbarHostState.showSnackbar("중복된 이름이 있습니다.")
                            else{
                                tagViewModel.insertTag()
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