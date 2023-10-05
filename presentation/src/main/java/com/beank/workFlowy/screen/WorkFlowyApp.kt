package com.beank.workFlowy.screen

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.beank.workFlowy.component.PermissionDialog
import com.beank.workFlowy.component.RationaleDialog
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.navigation.NavigationGraph
import com.beank.workFlowy.ui.theme.WorkFlowyTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkFlowyApp() {
    WorkFlowyTheme {
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
        if (permissionState.status.shouldShowRationale) RationaleDialog()
        else PermissionDialog { permissionState.launchPermissionRequest() }
    }
}