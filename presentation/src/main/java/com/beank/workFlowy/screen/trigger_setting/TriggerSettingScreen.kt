package com.beank.workFlowy.screen.trigger_setting

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.beank.domain.model.Tag
import com.beank.presentation.R
import com.beank.workFlowy.component.CustomAlertDialog
import com.beank.workFlowy.component.TextTopBar
import com.beank.workFlowy.component.TimeRangePickerDialog
import com.beank.workFlowy.component.VerticalSpacer
import com.beank.workFlowy.component.WeekLayout
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.screen.RequestLocationPermissionDialog
import com.beank.workFlowy.utils.getAddress
import com.beank.workFlowy.utils.intToImage
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriggerSettingScreen(
    triggerSettingViewModel: TriggerSettingViewModel = hiltViewModel(),
    snackbarHostState : SnackbarHostState,
    onBack : () -> Unit,
){
    val context = LocalContext.current
    val uiState = triggerSettingViewModel.uiState
    val configuration = LocalConfiguration.current
    val scrollState = rememberScrollState()
    val tagScrollState = rememberLazyListState()
    var showTimeState by rememberSaveable { mutableStateOf(false) }
    var tagToggle by remember { mutableStateOf(false)}
    var showPicker by remember { mutableStateOf(false) }
    var mapToggle by remember { mutableStateOf(false)}
    val startTimePickerState = rememberTimePickerState(
        initialHour = uiState.startTime.hour,
        initialMinute = uiState.startTime.minute,
        is24Hour = true
    )
    val endTimePickerState = rememberTimePickerState(
        initialHour = uiState.startTime.hour,
        initialMinute = uiState.startTime.minute,
        is24Hour = true
    )
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(LatLng(uiState.latitude,uiState.lonitude),18f)
    }
    val mapUiSetting by remember { mutableStateOf(MapUiSettings(
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false
    )) }
    val fusedLocationProviderClient = remember {LocationServices.getFusedLocationProviderClient(context)}

    LaunchedEffect(key1 = Unit){
        if (uiState.id.isEmpty()){
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnSuccessListener {
                    triggerSettingViewModel.onLatLngUpdate(LatLng(it.latitude,it.longitude))
                    cameraPositionState.move(CameraUpdateFactory.newLatLng(LatLng(it.latitude,it.longitude)))
                }
            }
        }
    }

    WeekLayout(
        snackbarHostState = snackbarHostState,
        topBar = {
            TextTopBar(
                title = if (uiState.id.isEmpty()) "트리거 등록" else "트리거 수정",
                onCancle = onBack,
                onConfirm = remember{{
                    if (uiState.id.isEmpty())
                        triggerSettingViewModel.onTriggerAdd()
                    else
                        triggerSettingViewModel.onTriggerUpdate()
                    onBack()
                }}
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            //태그 선택 화면
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (tagToggle) 250.dp else 85.dp)
                    .padding(10.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 5.dp, horizontal = 15.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = remember {
                                { tagToggle = tagToggle.not() }
                            })
                    ) {
                        Row {
                            Icon(
                                imageVector = ImageVector.vectorResource(intToImage(uiState.tagImage, LocalContext.current.resources.obtainTypedArray(R.array.tagList))),
                                contentDescription = "태그 사진",
                                modifier = Modifier
                                    .size(50.dp),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(text = uiState.tag, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.offset(x = 20.dp, y = 10.dp))
                        }
                        Icon(
                            imageVector = if (tagToggle) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                            contentDescription = "아이콘 선택",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier
                                .size(35.dp)
                                .padding(end = 12.dp)
                        )
                    }
                    if (tagToggle){
                        LazyColumn(
                            state = tagScrollState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 5.dp)
                                .height(150.dp)
                        ){
                            items(uiState.tagList, key = {it.id!!}){tag ->
                                TagItem(tag = tag, onClick = triggerSettingViewModel::onTagSelect)
                            }
                        }

                    }
                }
            }

            //지도 위치 선택
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(10.dp)
            ) {
                Column {
                    Button(
                        onClick = { mapToggle = true },
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Text(text = "위치 선택")
                    }
                    GoogleMap(
                        uiSettings = mapUiSetting,
                        cameraPositionState = cameraPositionState
                    ) {
                        Circle(
                            center = LatLng(uiState.latitude,uiState.lonitude),
                            radius = uiState.radius,
                            fillColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    }
                }
            }



        }

        if (mapToggle){
            GoogleMapDialog(
                latLng = LatLng(uiState.latitude,uiState.lonitude),
                radius = uiState.radius,
                context = context,
                onCancle = {mapToggle = false},
                onConfirm = {latlon,radius ->
                    triggerSettingViewModel.onLatLngUpdate(latlon,radius)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(uiState.latitude,uiState.lonitude),18f)
                }
            )
        }

        //시간 다이얼로그
        if (showTimeState) {
            TimeRangePickerDialog(
                onCancel = { showTimeState = false },
                onConfirm = {
                    if(triggerSettingViewModel.onTimeChange(startTimePickerState.hour,startTimePickerState.minute,endTimePickerState.hour,endTimePickerState.minute)){
                        showTimeState = false
                    }else{
                        SnackbarManager.showMessage(R.string.time_error)
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
    }
}

@Composable
fun TagItem(
    tag: Tag,
    onClick : (Tag) -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(onClick = { onClick(tag) })
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(intToImage(tag.icon, LocalContext.current.resources.obtainTypedArray(R.array.tagList))),
            contentDescription = "태그 사진",
            modifier = Modifier
                .size(50.dp),
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(text = tag.title, style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(start = 15.dp)
        )
    }
}



@Composable
fun GoogleMapDialog(
    latLng: LatLng,
    radius : Double,
    context : Context,
    onCancle : () -> Unit,
    onConfirm : (LatLng,Double) -> Unit
){
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(latLng,18f)
    }
    val mapUiSettings by remember {
        mutableStateOf(MapUiSettings(
            myLocationButtonEnabled = true,
            indoorLevelPickerEnabled = true
        ))}
    var selectRadius by remember { mutableDoubleStateOf(radius) }
    var sliderState by remember { mutableFloatStateOf(3f) }
    var selectLatLng by remember { mutableStateOf(latLng)}
    var locationName by remember { mutableStateOf("")}
    val geocoder = Geocoder(context, Locale.KOREA)
    val scope = rememberCoroutineScope()

    CustomAlertDialog(
        properties = DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true),
        onDismissRequest = onCancle
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
        ){
            Column {
                TextTopBar(title = "위치 선택", onCancle = onCancle, onConfirm = {
                    onConfirm(selectLatLng,selectRadius)
                    onCancle()
                })
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                        .height(80.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    TextField(
                        modifier = Modifier
                            .width(250.dp),
                        value = locationName,
                        onValueChange = {
                            locationName = it
                        },
                        placeholder = { Text(text = "주소를 입력하세요.")},
                        textStyle = MaterialTheme.typography.bodyLarge
                    )
                    IconButton(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(80.dp),
                        onClick = {
                        geocoder.getAddress(locationName){
                            it?.let {
                                selectLatLng = LatLng(it.latitude,it.longitude)
                                scope.launch {
                                    cameraPositionState.animate(CameraUpdateFactory.newLatLng(selectLatLng),500)
                                }
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "검색")
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Slider(
                        value = sliderState,
                        valueRange = 0f..30f,
                        onValueChange = {sliderState = it},
                        onValueChangeFinished = {
                            selectRadius = 10.0 + sliderState*10
                            cameraPositionState.move(CameraUpdateFactory.zoomTo(18f - sliderState/10))
                        },
                        steps = 31
                    )
                }
                VerticalSpacer(height = 5.dp)
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true, isBuildingEnabled = true, isIndoorEnabled = true),
                    onMapClick = {
                        selectLatLng = it
                    },
                    onPOIClick = {
                        selectLatLng = it.latLng
                    },
                    uiSettings = mapUiSettings
                ) {
                    Circle(
                        center = selectLatLng,
                        radius = selectRadius,
                        strokeColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }


            }
        }
    }
}
