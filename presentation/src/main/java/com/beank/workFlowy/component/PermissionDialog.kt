package com.beank.workFlowy.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.beank.presentation.R.string as AppText

@Composable
fun PermissionDialog(onRequestPermission: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(true) }

    if (showWarningDialog) {
        AlertDialog(
            modifier = Modifier.alertDialog(),
            title = { Text(stringResource(id = AppText.notification_permission_title)) },
            text = { Text(stringResource(id = AppText.notification_permission_description)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRequestPermission()
                        showWarningDialog = false
                    },
                    modifier = Modifier.textButton(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) { Text(text = stringResource(AppText.request_notification_permission)) }
            },
            onDismissRequest = { }
        )
    }
}

@Composable
fun RationaleDialog() {
    var showWarningDialog by remember { mutableStateOf(true) }

    if (showWarningDialog) {
        AlertDialog(
            modifier = Modifier.alertDialog(),
            title = { Text(stringResource(id = AppText.notification_permission_title)) },
            text = { Text(stringResource(id = AppText.notification_permission_settings)) },
            confirmButton = {
                TextButton(
                    onClick = { showWarningDialog = false },
                    modifier = Modifier.textButton(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) { Text(text = stringResource(AppText.ok)) }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}