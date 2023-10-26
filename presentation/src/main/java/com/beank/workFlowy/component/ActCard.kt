package com.beank.workFlowy.component

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.beank.domain.model.Tag
import com.beank.presentation.R
import com.beank.workFlowy.utils.intToImage
import com.beank.workFlowy.utils.zeroFormat
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActCard(
    selectedTag: () -> Tag,
    progressTime : () -> Duration,
    progress : () -> Boolean,
    onClickAct : () -> Unit
){
    Button(
        onClick = onClickAct,
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(15.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        elevation = ButtonDefaults.elevatedButtonElevation(5.dp),
    ) {
        if (progress()){
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        AnimatedVisibility(visible = !progress()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = intToImage(selectedTag().icon, LocalContext.current.resources.obtainTypedArray(R.array.tagList))),
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Row {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = 25.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = selectedTag().title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis)
                        Text(
                            text = "${zeroFormat.format(progressTime().toHours())}:${zeroFormat.format(progressTime().toMinutes()%60)}:${zeroFormat.format(progressTime().seconds%60)}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .height(30.dp)
                                .padding(start = 5.dp))

                    }
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
    val backgroundColors by animateColorAsState(targetValue = if (clicked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondaryContainer,
        label = "선택 날짜 색상"
    )

    Card(
        modifier = Modifier
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
                .fillMaxWidth()
                .height(100.dp)
                .drawBehind {
                    drawRoundRect(color = backgroundColors, size = size, cornerRadius = CornerRadius(8f) )
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = clicked) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_close_24),
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
                        imageVector = ImageVector.vectorResource(id = intToImage(tag.icon, LocalContext.current.resources.obtainTypedArray(R.array.tagList))),
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