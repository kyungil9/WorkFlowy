package com.example.workFlowy.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.Tag
import com.example.workFlowy.R
import com.example.workFlowy.ui.theme.lightRed
import com.example.workFlowy.ui.theme.white
import com.example.workFlowy.utils.intToImage
import java.time.Duration

@Composable
fun ActCard(
    selectedTag: Tag,
    progressTime : Duration,
    onClickAct : () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(20.dp)
            .clickable { onClickAct() },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Image(
                painter = painterResource(id = intToImage(selectedTag.icon, LocalContext.current.resources.obtainTypedArray(R.array.tagList))),
                contentDescription = null,
                modifier = Modifier
                    .padding(15.dp)
                    .size(150.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = selectedTag.title, fontSize = 32.sp, modifier = Modifier.padding(vertical = 20.dp))
                Text(
                    text = "${progressTime.toHours()}:${progressTime.toMinutes()%60}:${progressTime.seconds%60}",
                    fontSize = 24.sp)
            }
        }
    }
}

@Composable
fun ActTagCard(
    tag : Tag,
    onClickAct: (Tag) -> Unit,
    onClickDelect: (Tag) -> Unit
){
    var clicked by remember { mutableStateOf(false) }
    val backgroundColors by animateColorAsState(targetValue = if (clicked) lightRed else white)

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (clicked) {
                            onClickDelect(tag)
                            clicked = clicked.not()
                        }else
                            onClickAct(tag) },
                    onLongPress = { clicked = clicked.not() }
                )
            },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .background(backgroundColors, shape = RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .height(100.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = clicked) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_close_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                )
            }
            AnimatedVisibility(visible = clicked.not()) {
                Column(
                    modifier = Modifier
                        .height(100.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = intToImage(tag.icon, LocalContext.current.resources.obtainTypedArray(R.array.tagList))),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                    )

                    Text(text = tag!!.title, fontSize = 24.sp)
                }
            }
        }
    }

}