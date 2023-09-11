package com.beank.workFlowy.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.Hours
import com.chargemap.compose.numberpicker.HoursNumberPicker
import com.chargemap.compose.numberpicker.NumberPicker

@Composable
fun DaysPicker(
    timeVisibility : Boolean,
    yearPick : Int,
    monthPick : Int,
    dayPick : Int,
    timePick : FullHours,
    endDay : Int,
    onYearChange : (Int) -> Unit,
    onMonthChange : (Int) -> Unit,
    onDayChange : (Int) -> Unit,
    onTimeChange : (Hours) -> Unit,
){


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
    ) {
        NumberPicker(
            value = yearPick,
            onValueChange = onYearChange,
            range = 2022..2025,
            dividersColor = MaterialTheme.colorScheme.primary
        )
        NumberPicker(
            value = monthPick,
            onValueChange = onMonthChange,
            range = 1..12,
            dividersColor = MaterialTheme.colorScheme.primary
        )
        NumberPicker(
            value = dayPick,
            onValueChange = onDayChange,
            range = 1..endDay,
            dividersColor = MaterialTheme.colorScheme.primary
        )

        AnimatedVisibility(visible = timeVisibility) {
            HoursNumberPicker(
                value = timePick,
                onValueChange = onTimeChange,
                hoursDivider = {
                    Text(text = ":" , textAlign = TextAlign.Center)
                }
            )
        }
    }
}
