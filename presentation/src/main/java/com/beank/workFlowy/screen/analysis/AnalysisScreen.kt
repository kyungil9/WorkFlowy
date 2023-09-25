package com.beank.workFlowy.screen.analysis

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.beank.presentation.R
import com.beank.workFlowy.component.IconButton
import com.beank.workFlowy.component.StackBar
import com.beank.workFlowy.component.WeekLayout
import com.beank.workFlowy.utils.toFormatString
import com.beank.workFlowy.utils.toMonthString
import com.beank.workFlowy.utils.toWeekString
import com.beank.workFlowy.utils.toYearString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.math.abs

@Composable
fun AnalysisScreen(
    analysisViewModel: AnalysisViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onBack : () -> Unit
){
    val uiState = analysisViewModel.uiState
    val selectDay by analysisViewModel.selectDayFlow.collectAsStateWithLifecycle()
    val toggleFlow by analysisViewModel.toggleButtonFlow.collectAsStateWithLifecycle()
    val animate by analysisViewModel.animateStackChannel.collectAsStateWithLifecycle(initialValue = false)

    WeekLayout(snackbarHostState = snackbarHostState) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RecordCard(
                uiState = uiState,
                actProgress = analysisViewModel.actBoxProgressFlow,
                selectDay = selectDay,
                toggle = toggleFlow,
                animate = animate,
                updateDay = analysisViewModel::changeSelectDay,
                updateToggle = analysisViewModel::updateToggleButton,
                updateAnimate = analysisViewModel::sendAnimationEvent,
                onRightDrag = {},
                onLeftDrag = {}
            )


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordCard(
    uiState: AnalysisUiState,
    actProgress: Boolean,
    selectDay: LocalDate,
    toggle : Int,
    animate : Boolean,
    updateDay : (LocalDate) -> Unit,
    updateToggle : () -> Unit,
    updateAnimate : (Boolean) -> Unit,
    onRightDrag: () -> Unit,
    onLeftDrag: () -> Unit,
){
    val list = listOf("일별","주별","월별","년별")
    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    var direction by remember { mutableIntStateOf(-1) }

    val dateDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, day ->
            updateDay(LocalDate.of(year,month+1,day))
        },selectDay.year,
        selectDay.monthValue-1,
        selectDay.dayOfMonth
    )
    dateDialog.datePicker.minDate = LocalDate.of(2021,12,28).atTime(0,0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    dateDialog.datePicker.maxDate = LocalDate.of(2026,1,3).atTime(0,0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val (x, y) = dragAmount
                        if (abs(x) > abs(y)) {
                            when {
                                x > 0 -> {
                                    dragOffsetX += x
                                    direction = 0
                                }

                                x < 0 -> {
                                    dragOffsetX += x
                                    direction = 1
                                }
                            }
                        }
                    },
                    onDragEnd = {
                        when (direction) {
                            0 -> {
                                if (dragOffsetX > 400) {
                                    //left motion
                                    onLeftDrag()
                                }
                                dragOffsetX = 0f
                            }

                            1 -> {
                                if (dragOffsetX < -400) {
                                    //right
                                    onRightDrag()

                                }
                                dragOffsetX = 0f
                            }
                        }
                    }
                )
            },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .offset(y = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                IconButton(
                    icon = R.drawable.baseline_calendar_today_24,
                    text = when (toggle){
                        0 -> selectDay.toFormatString()
                        1 -> selectDay.toWeekString()
                        2 -> selectDay.toMonthString()
                        3 -> selectDay.toYearString()
                        else -> ""
                    },
                    modifier = Modifier.size(150.dp,40.dp)
                ) {
                    //updateAnimate(false)
                    //다이얼로그 띄우기
                    dateDialog.show()
                }

                AssistChip(
                    onClick = updateToggle,
                    label = {
                        Text(text = list[toggle])
                    },
                    modifier = Modifier.height(40.dp)
                )

            }
            if (actProgress){
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            AnimatedVisibility(visible = !actProgress) {
                LaunchedEffect(key1 = selectDay){
                    updateAnimate(true)
                }
                StackBar(
                    record = uiState.recordList,
                    strokeWidth = 50f,
                    animate = animate
                )
            }
        }


    }
}
