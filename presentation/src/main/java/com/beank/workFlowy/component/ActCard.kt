package com.beank.workFlowy.component

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.beank.domain.model.Tag
import com.beank.presentation.R
import com.beank.workFlowy.ui.theme.WorkFlowyTheme
import com.beank.workFlowy.ui.theme.lightRed
import com.beank.workFlowy.ui.theme.white
import com.beank.workFlowy.utils.intToImage
import com.beank.workFlowy.utils.zeroFormat
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActCard(
    selectedTag: Tag,
    progressTime : Duration,
    progress : Boolean,
    onClickAct : () -> Unit
){
    Button(
        onClick = onClickAct,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(20.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
        elevation = ButtonDefaults.elevatedButtonElevation(5.dp),
    ) {
        if (progress){
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        AnimatedVisibility(visible = !progress) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = intToImage(selectedTag.icon, LocalContext.current.resources.obtainTypedArray(R.array.tagList))),
                    contentDescription = null,
                    modifier = Modifier
                        .size(130.dp),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 15.dp,end = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = selectedTag.title, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(vertical = 20.dp))
                    Text(
                        text = "${zeroFormat.format(progressTime.toHours())}:${zeroFormat.format(progressTime.toMinutes()%60)}:${zeroFormat.format(progressTime.seconds%60)}",
                        style = MaterialTheme.typography.headlineSmall)
                }
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
    val backgroundColors by animateColorAsState(targetValue = if (clicked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondaryContainer)

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
                        } else
                            onClickAct(tag)
                    },
                    onLongPress = { clicked = clicked.not() }
                )
            },
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .background(backgroundColors, shape = MaterialTheme.shapes.small)
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
                        .size(80.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            AnimatedVisibility(visible = clicked.not()) {
                Column(
                    modifier = Modifier
                        .height(100.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = intToImage(tag.icon, LocalContext.current.resources.obtainTypedArray(R.array.tagList))),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(text = tag.title, style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
    }

}