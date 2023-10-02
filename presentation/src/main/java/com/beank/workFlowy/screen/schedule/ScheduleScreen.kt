package com.beank.workFlowy.screen.schedule

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.beank.presentation.R
import com.beank.workFlowy.component.TimeRangePickerDialog
import com.beank.workFlowy.component.VerticalSpacer
import com.beank.workFlowy.component.WeekAppBar
import com.beank.workFlowy.component.WeekLayout
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.ui.theme.gray
import com.beank.workFlowy.ui.theme.white
import com.beank.workFlowy.utils.toFormatShortString
import com.beank.workFlowy.utils.toFormatString
import com.beank.workFlowy.utils.toLocalDateTime
import com.beank.workFlowy.utils.toStartTimeLong
import com.beank.presentation.R.string as AppText


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleScreen(
    scheduleViewModel: ScheduleViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onBackHome : () -> Unit
) {
    val resources = LocalContext.current.resources
    val uiState = scheduleViewModel.uiState
    val selectStartTime = uiState.startTime
    val selectEndTime = uiState.endTime
    val configuration = LocalConfiguration.current
    val startTimePickerState = rememberTimePickerState(
        initialHour = selectStartTime.hour,
        initialMinute = selectStartTime.minute,
        is24Hour = true
    )
    val endTimePickerState = rememberTimePickerState(
        initialHour = selectStartTime.hour,
        initialMinute = selectStartTime.minute,
        is24Hour = true
    )
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.date.toStartTimeLong(),
        yearRange = (2022..2025)
    )
    var showDateState by rememberSaveable { mutableStateOf(false) }
    var showTimeState by rememberSaveable { mutableStateOf(false) }
    var showPicker by remember { mutableStateOf(false) }
    var imageToggle by remember { mutableStateOf(false)}

    LaunchedEffect(key1 = Unit) {
        scheduleViewModel.initScheduleImages(resources.obtainTypedArray(R.array.scheduleList))
    }

    WeekLayout(
        snackbarHostState = snackbarHostState,
        topBar = {
            WeekAppBar(
                headerIcon = R.drawable.baseline_close_24,
                onHeaderIconClick = onBackHome,
                selectDay = "일정",
                tailIcon = R.drawable.baseline_check_24,
                onTailIconClick = {
                    if (uiState.id.isNotEmpty())
                        scheduleViewModel.onScheduleUpdate()
                    else
                        scheduleViewModel.onScheduleInsert()
                    onBackHome()
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp)
            ) {
                TextField(
                    value = uiState.title,
                    onValueChange = scheduleViewModel::onTitleChange,
                    label = { Text(text = "일정") }
                )
                Image(
                    painter = painterResource(id = uiState.image),
                    contentDescription = "스캐줄사진",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            imageToggle = true
                        }
                )
            }
            AnimatedVisibility(visible = imageToggle) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(40.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(space = 5.dp),
                    contentPadding = PaddingValues(all = 5.dp)
                ) {
                    items(uiState.scheduleImageList) { image ->
                        Image(
                            painter = painterResource(id = image),
                            contentDescription = "스캐줄사진리스트",
                            modifier = Modifier
                                .height(40.dp)
                                .clickable {
                                    scheduleViewModel.onImageChange(image)
                                }
                        )
                    }
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = gray
            )

            //날짜 시간 변경
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (uiState.timeToggle) 115.dp else 85.dp)
                    .padding(10.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiaryContainer),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 5.dp, horizontal = 15.dp)
                ) {
                    Row {
                        Column(
                            verticalArrangement = Arrangement.Center
                        ) {
                            TextButton(onClick = { showDateState = true }) {
                                Text(
                                    text = uiState.date.toFormatString(),
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                            if(uiState.timeToggle) {
                                TextButton(
                                    onClick = { showTimeState = true },
                                    modifier = Modifier.offset(x = 15.dp, y = (-10).dp)
                                ) {
                                    Text(
                                        text = "${selectStartTime.hour}:${selectStartTime.minute} ~ ${selectEndTime.hour}:${selectEndTime.minute}",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                            }
                        }
                    }
                    IconButton(onClick = { scheduleViewModel.onTimeToggleChange() }) {
                        Icon(
                            imageVector = if (uiState.timeToggle) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                            contentDescription = "시간 선택",
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }



            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                    .background(white, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                value = uiState.comment,
                onValueChange = scheduleViewModel::onCommentChange,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                maxLines = 5
            )
        }

        //시간 다이얼로그
        if (showTimeState) {
            TimeRangePickerDialog(
                onCancel = { showTimeState = false },
                onConfirm = {
                    if(scheduleViewModel.onTimeChange(startTimePickerState.hour,startTimePickerState.hour,endTimePickerState.hour,endTimePickerState.minute)){
                        showTimeState = false
                    }else{
                        SnackbarManager.showMessage(AppText.time_error)
                    }
                },
                toggle = {
                    if (configuration.screenHeightDp > 400) {
                        IconButton(onClick = { showPicker = !showPicker }) {
                            val icon = if (showPicker) {
                                Icons.Outlined.KeyboardArrowUp
                            } else {
                                Icons.Outlined.DateRange
                            }
                            Icon(
                                icon,
                                contentDescription = if (showPicker) {
                                    "Switch to Text Input"
                                } else {
                                    "Switch to Touch Input"
                                }
                            )
                        }
                    }
                },
                startTimeContent = {
                    if (showPicker && configuration.screenHeightDp > 400) {
                        TimePicker(state = startTimePickerState)
                    } else {
                        TimeInput(state = startTimePickerState)
                    }
                },
                endTimeContent = {
                    if (showPicker && configuration.screenHeightDp > 400) {
                        TimePicker(state = endTimePickerState)
                    } else {
                        TimeInput(state = endTimePickerState)
                    }
                }
            )
        }
        //날짜 다이얼로그
        if (showDateState) {
            DatePickerDialog(
                onDismissRequest = { showDateState = false },
                confirmButton = {
                    TextButton(onClick = {
                        val date = datePickerState.selectedDateMillis?.toLocalDateTime()
                        date?.let {
                            scheduleViewModel.onDateChange(date.toLocalDate())
                        }
                        showDateState = false
                    }) {
                        Text(text = "확인")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDateState = false }) {
                        Text(text = "취소")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    title = {
                        Text(
                            text = "날짜 선택",
                            modifier = Modifier.padding(top = 16.dp, start = 24.dp, end = 12.dp),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    headline = {
                        Text(
                            text = datePickerState.selectedDateMillis?.toLocalDateTime()
                                ?.toLocalDate()?.toFormatShortString() ?: "",
                            modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 12.dp)
                        )
                    }
                )
            }

        }
    }
}

@Preview
@Composable
fun PreviewMainContainer() {
    WeekLayout(snackbarHostState = SnackbarHostState()) {
        Column() {
            VerticalSpacer(height = 10.dp)
            HorizontalDivider(
                thickness = 0.5.dp,
                color = gray
            )
        }
    }
}