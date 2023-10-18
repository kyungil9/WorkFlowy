package com.beank.workFlowy.screen

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.beank.workFlowy.component.PermissionDialog
import com.beank.workFlowy.component.RationaleDialog
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.navigation.NavigationGraph
import com.beank.workFlowy.ui.theme.WorkFlowyTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope
import com.beank.presentation.R.string as AppText

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkFlowyApp() {
    WorkFlowyTheme {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ){
            RequestLocationPermissionDialog()
            RequestTransitionPermissionDialog()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ) {
            RequestNotificationPermissionDialog()
        }
        Surface {
            val appState = rememberAppState()
            NavigationGraph(appState)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navController: NavHostController = rememberNavController(),
    resources: Resources = resources(),
    snackbarManager : SnackbarManager = SnackbarManager,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(snackbarHostState,navController,resources,snackbarManager,coroutineScope) {
    WorkFlowyState(snackbarHostState,navController,resources,snackbarManager,coroutineScope)
}

@Composable
@ReadOnlyComposable
fun resources() : Resources = LocalContext.current.resources

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RequestNotificationPermissionDialog() {
    val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    if (!permissionState.status.isGranted) {
        if (permissionState.status.shouldShowRationale) RationaleDialog(stringResource(id = AppText.notification_permission_title),stringResource(id = AppText.notification_permission_description))
        else PermissionDialog(stringResource(id = AppText.notification_permission_title),stringResource(id = AppText.notification_permission_description)) { permissionState.launchPermissionRequest() }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun RequestLocationPermissionDialog() {
    val foregroundPermissionState = rememberMultiplePermissionsState(permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    ))
    if (!foregroundPermissionState.allPermissionsGranted){
        if (foregroundPermissionState.shouldShowRationale)
            RationaleDialog(stringResource(id = AppText.location_permission_title),stringResource(id = AppText.location_permission_description))
        else
            PermissionDialog(stringResource(id = AppText.location_permission_title),stringResource(id = AppText.location_permission_description)) { foregroundPermissionState.launchMultiplePermissionRequest() }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun RequestTransitionPermissionDialog() {
    val permissionState = rememberPermissionState(permission = Manifest.permission.ACTIVITY_RECOGNITION)
    if (!permissionState.status.isGranted){
        if (permissionState.status.shouldShowRationale)
            RationaleDialog(stringResource(id = AppText.transition_title),stringResource(id = AppText.transition_description))
        else
            PermissionDialog(stringResource(id = AppText.transition_title),stringResource(id = AppText.transition_description)) { permissionState.launchPermissionRequest() }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun RequestBackgroundPermissionDialog() {
    val backgroundPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    if (!backgroundPermissionState.status.isGranted) {
        if (backgroundPermissionState.status.shouldShowRationale)
            RationaleDialog(stringResource(id = AppText.background_permission_title), stringResource(id = AppText.background_permission_description)){
                backgroundPermissionState.launchPermissionRequest()
            }
        else {
            PermissionDialog(stringResource(id = AppText.background_permission_title), stringResource(id = AppText.background_permission_description)) {
                backgroundPermissionState.launchPermissionRequest()
            }
        }
    }

}


