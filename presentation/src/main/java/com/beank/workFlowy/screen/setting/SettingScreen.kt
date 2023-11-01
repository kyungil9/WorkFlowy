package com.beank.workFlowy.screen.setting


import android.net.Uri
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.beank.presentation.R
import com.beank.workFlowy.component.BackTopBar
import com.beank.workFlowy.component.SettingCard
import com.beank.workFlowy.component.TextCard
import com.beank.workFlowy.component.ToggleCard
import com.beank.workFlowy.component.VerticalSpacer
import com.beank.workFlowy.component.WeekLayout
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.navigation.NavigationItem
import com.beank.workFlowy.screen.RequestBackgroundPermissionDialog
import com.beank.workFlowy.screen.RequestLocationPermissionDialog
import com.beank.workFlowy.screen.RequestNotificationPermissionDialog
import com.beank.workFlowy.screen.RequestTransitionPermissionDialog
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.beank.presentation.R.string as AppText

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SettingScreen(
    settingViewModel: SettingViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onBackClear : (String) -> Unit,
    onBack : () -> Unit,
    onMove : (String) -> Unit
){
    val launcher = rememberImageLauncher(onImageSuccess = {
        settingViewModel.onImageUpload(it) {
            SnackbarManager.showMessage(AppText.firebase_server_error)
        }
    })
    val scrollState = rememberScrollState()
    var editNickname by remember { mutableStateOf(false)}
    val uiState = settingViewModel.uiState
    val onSignOut = remember{
        {
            settingViewModel.signOut()
            onBackClear(NavigationItem.LOGIN.route)
        }
    }

    WeekLayout(
        snackbarHostState = snackbarHostState,
        topBar = remember{{
            BackTopBar(title = "설정", onBack = onBack)
        }}
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            verticalArrangement = Arrangement.Top
        ) {
            Card(//유저 프로필 화면
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(10.dp),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer),
                elevation = CardDefaults.elevatedCardElevation(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.padding(start = 30.dp, top = 25.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row() {
                            if (editNickname){
                                OutlinedTextField(
                                    label = { Text(text = "닉네임") },
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(60.dp)
                                        .background(
                                            MaterialTheme.colorScheme.secondaryContainer,
                                            RoundedCornerShape(5.dp)
                                        ),
                                    shape = MaterialTheme.shapes.small,
                                    value = uiState.tempNickname,
                                    onValueChange = {
                                        if (it.length <= 10)
                                            settingViewModel.onTempNicknameUpdate(it)//해당 부분 문제
                                        else
                                            SnackbarManager.showMessage(AppText.max_length)
                                    },
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                                    singleLine = true,
                                    maxLines = 1
                                )
                            }else{
                                Text(text = uiState.nickname, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
                            }
                            IconButton(onClick = remember{{
                                if (editNickname)
                                    settingViewModel.onNicknameUpdate()
                                else
                                    settingViewModel.onNicknameRefresh()
                                editNickname = editNickname.not()
                            }}) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = if (editNickname) R.drawable.baseline_check_24 else R.drawable.baseline_edit_24),
                                    contentDescription = "닉네임 수정",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }

                        VerticalSpacer(height = 10.dp)
                        Text(
                            text = when (uiState.grade) {
                                0 -> "브론즈"
                                1 -> "실버"
                                2 -> "골드"
                                3 -> "플레티넘"
                                4 -> "다이아"
                                5 -> "성실왕"
                                else -> ""
                            },
                            style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Column (
                        modifier = Modifier.padding(top = 15.dp, end = 35.dp),
                        verticalArrangement = Arrangement.Center
                    ){
                        if (uiState.userProgress){
                            CircularProgressIndicator()
                        }else {
                            GlideImage(
                                model = uiState.urlImage,
                                contentDescription = "프로필 사진",
                                contentScale = ContentScale.Crop,
                                loading = placeholder(R.drawable.baseline_downloading_24),
                                failure = placeholder(R.drawable.baseline_error_outline_24),
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        launcher.launch(
                                            PickVisualMediaRequest(
                                                ActivityResultContracts.PickVisualMedia.ImageOnly
                                            )
                                        )
                                    }
                            )
                        }
                    }
                }
            }
            VerticalSpacer(height = 20.dp)

            Column(modifier = Modifier.verticalScroll(scrollState)) {
                TextCard(title = stringResource(id = AppText.alarmSetting))
                ToggleCard(title = stringResource(id = AppText.noticeAlarm), checked = {uiState.noticeToggle}, onClick = settingViewModel::onNoticeAlarmUpdate)
                ToggleCard(title = stringResource(id = AppText.scheduleAlarm), checked = {uiState.scheduleToggle}, onClick = settingViewModel::onScheduleAlarmUpdate)

                VerticalSpacer(height = 10.dp)
                TextCard(title = stringResource(id = AppText.record_widget))
                ToggleCard(title = stringResource(id = AppText.record_widget_setting), checked = {uiState.recordAlarmToggle}, onClick = settingViewModel::onRecordAlarmUpdate)


                VerticalSpacer(height = 10.dp)
                TextCard(title = stringResource(id = AppText.themeSetting))
                ToggleCard(title = stringResource(id = AppText.darkTheme), checked = {uiState.darkThemeToggle}, onClick = settingViewModel::onDarkThemeUpdate)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ToggleCard(title = stringResource(id = AppText.dynamicTheme), checked = {uiState.dynamicThemeToggle}, onClick = settingViewModel::onDynamicThemeUpdate)
                }
                VerticalSpacer(height = 10.dp)

                SettingCard(title = stringResource(id = AppText.triggerTitle))
                ToggleCard(title = stringResource(id = AppText.triggerToggle), checked = {uiState.triggerToggle}, onClick = settingViewModel::onTriggerToggleUpdate)
                ToggleCard(title = stringResource(id = AppText.triggerMove), checked = {uiState.triggerMoveToggle}, onClick = settingViewModel::onTriggerMoveUpdate)
                SettingCard(title = stringResource(id = AppText.triggerSetting), padding = 25.dp, onClick = {onMove(NavigationItem.TRIGGER.route)})
                VerticalSpacer(height = 5.dp)

                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
                VerticalSpacer(height = 5.dp)

                SettingCard(title = stringResource(id = AppText.notice))
                SettingCard(title = stringResource(id = AppText.customCenter))
                //SettingCard(title = stringResource(id = AppText.useOfTerm))
                TextCard(title = stringResource(id = AppText.version), comment = LocalContext.current.packageManager.getPackageInfo(
                    LocalContext.current.packageName,0).versionName)

                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
                VerticalSpacer(height = 5.dp)

                SettingCard(title = stringResource(id = AppText.logout), color = MaterialTheme.colorScheme.error, onClick = onSignOut)

            }
        }
    }
    if ((uiState.noticeToggle || uiState.scheduleToggle) &&  Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ){
        RequestNotificationPermissionDialog()
    }
    if (uiState.triggerToggle && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            RequestBackgroundPermissionDialog()
        RequestLocationPermissionDialog()
        RequestTransitionPermissionDialog()
    }
}

@Composable
fun rememberImageLauncher(
    onImageSuccess : (Uri) -> Unit
) : ManagedActivityResultLauncher<PickVisualMediaRequest,Uri?> {
    return rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) onImageSuccess(uri)
    }
}