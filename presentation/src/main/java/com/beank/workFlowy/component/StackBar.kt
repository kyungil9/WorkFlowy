package com.beank.workFlowy.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.dp
import com.beank.domain.model.Record
import com.beank.workFlowy.ui.theme.colors
import com.beank.workFlowy.utils.animateTargetFloatAsState
import com.beank.workFlowy.utils.toLong

@Composable
fun StackBar(
    record: List<Record>,//크기순으로 받아오기
    strokeWidth: Float,
    cornerRadius: CornerRadius = CornerRadius(strokeWidth),
    animate : Boolean,
    modifier: Modifier = Modifier
){

    val sumOfData = remember(record) {
        record.map {
            it.progressTime
        }.sum()
    }
    val animationProgress by animateTargetFloatAsState(
        initialValue = 1f,
        targetValue = if(animate) 1f else 0f,
        tween(2000,500)
    )

    Column(
        modifier = Modifier
    ) {
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
        ){
            val lineStart = size.width * 0.05f
            val lineEnd = size.width * 0.95f
            val lineLength = (lineEnd - lineStart) * animationProgress
            val lineHeightOffset = (size.height - strokeWidth) * 0.6f

            val path = Path().apply{
                addRoundRect(
                    RoundRect(
                        Rect(
                            offset = Offset(lineStart,lineHeightOffset),
                            size = Size(lineLength,strokeWidth)
                        ),
                        cornerRadius
                    )
                )
            }
            clipPath(path){
                var dataStart = lineStart
                record.forEachIndexed { index, record ->
                    val dataEnd = dataStart + (((record.progressTime).toFloat() / sumOfData) * lineLength)
                    drawRect(
                        color = colors[index],
                        topLeft = Offset(dataStart, lineHeightOffset),
                        size = Size(dataEnd - dataStart, strokeWidth)
                    )
                    dataStart = dataEnd
                }
            }
        }
        LazyColumn(
            state = rememberLazyListState(),
            modifier = modifier
                .padding(horizontal = 20.dp)
                .height((10+30*record.size).dp)
                .animateContentSize(tween(1000))//height를 item 수만큼 해서 변동 3개기준 100
        ) {
            itemsIndexed(record){index,record ->
                if (record.progressTime != 0L)
                    StackItem(record = record, color = colors[index], total = sumOfData)
            }
        }
    }
}

@Composable
fun StackItem(
    record: Record,
    color: Color,
    total : Long
){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(30.dp,30.dp)){
            drawCircle(
                color = color,
                center = Offset(size.width/2,size.height/2),
                radius = size.minDimension / 4
            )
        }
        HorizontalSpacer(width = 20.dp)
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = record.tag)
            Text(text = "(${(record.progressTime*100)/total})")
        }
    }
}

