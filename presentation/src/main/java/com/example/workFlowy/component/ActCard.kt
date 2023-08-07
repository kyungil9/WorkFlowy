package com.example.workFlowy.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.local.database.entity.WeekTag
import com.example.domain.model.Tag
import com.example.workFlowy.R
import com.example.workFlowy.WeekViewModel
import java.time.Duration

@Composable
fun ActCard(
    selectedTag: Tag,
    progressTime : Duration,
    onClickAct : () -> Unit
){
    Card(
        Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(20.dp)
            .clickable { onClickAct() },
        shape = RoundedCornerShape(10.dp),
        elevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Image(
                painter = painterResource(id = selectedTag.icon),
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
    onClickAct: (Tag) -> Unit
){
    Card(
        Modifier
            .fillMaxSize()
            .padding(10.dp)
            .clickable { onClickAct(tag) },
        shape = RoundedCornerShape(10.dp),
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier
                .height(100.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = tag!!.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
            )

            Text(text = tag!!.title, fontSize = 24.sp)
        }
    }
}