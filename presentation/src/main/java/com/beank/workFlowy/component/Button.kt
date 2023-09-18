package com.beank.workFlowy.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beank.workFlowy.ui.theme.gray
import com.google.android.gms.base.R

@Composable
fun BasicTextButton(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    TextButton(onClick = action, modifier = modifier) { Text(text = stringResource(text)) }
}

@Composable
fun BasicButton(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    Button(
        onClick = action,
        modifier = modifier,
        colors =
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(text = stringResource(text), fontSize = 16.sp)
    }
}

@Composable
fun IconButton(
    @DrawableRes icon : Int,
    @StringRes text : Int,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.small,
        shadowElevation = 5.dp
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(
                start = 14.dp,
                end = 12.dp,
                top = 11.dp,
                bottom = 11.dp
            )
        ) {
            Icon(painter = painterResource(id = icon), contentDescription = "Google sign button", tint = Color.Unspecified, modifier = Modifier.size(35.dp))
            Text(text = stringResource(text), style = MaterialTheme.typography.headlineMedium, color = Color.Gray, fontSize = 17.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth() , textAlign = TextAlign.Center)
        }
    }
}

@Preview
@Composable
internal fun PreviewButtonContainer() {
    IconButton(icon = R.drawable.googleg_standard_color_18, text = R.string.common_signin_button_text) {

    }
}