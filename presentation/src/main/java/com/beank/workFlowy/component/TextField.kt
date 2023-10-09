package com.beank.workFlowy.component

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.beank.presentation.R.string as AppText
import com.beank.presentation.R.drawable as AppIcon

@Composable
fun BasicField(
    @StringRes text: Int,
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = onNewValue,
        placeholder = { Text(stringResource(text)) }
    )
}

@Composable
fun EmailField(value: () -> String, onNewValue: (String) -> Unit) {
    Log.d("email","text:${value.hashCode()},onclick:${onNewValue.hashCode()}")
    OutlinedTextField(
        singleLine = true,
        modifier = Modifier.fieldModifier(),
        value = value(),
        onValueChange = onNewValue,
        placeholder = { Text(stringResource(AppText.email)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") },
        maxLines = 1
    )
}

@Composable
fun PasswordField(value: () -> String, onNewValue: (String) -> Unit) {
    PasswordField(value, AppText.password, onNewValue)
}

@Composable
fun RepeatPasswordField(
    value: () -> String,
    onNewValue: (String) -> Unit
) {
    PasswordField(value, AppText.repeat_password, onNewValue)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordField(
    value: () -> String,
    @StringRes placeholder: Int,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier.fieldModifier(),
        value = value(),
        onValueChange = onNewValue,
        placeholder = { Text(text = stringResource(placeholder)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(imageVector =  if (isVisible) ImageVector.vectorResource(AppIcon.ic_visibility_on)
                else ImageVector.vectorResource(AppIcon.ic_visibility_off), contentDescription = "Visibility")
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        maxLines = 1
    )
}

