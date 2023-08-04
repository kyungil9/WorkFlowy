package com.example.workFlowy.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.example.workFlowy.R

@Composable
fun ActBox(){
    Card(
        Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(20.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_directions_run_24),
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
                Text(text = "운동중", fontSize = 32.sp, modifier = Modifier.padding(vertical = 20.dp))
                Text(
                    text = "00:00",
                    fontSize = 24.sp)
            }
        }
    }
}