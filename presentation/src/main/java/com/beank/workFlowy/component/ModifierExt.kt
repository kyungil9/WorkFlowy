package com.beank.workFlowy.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun Modifier.textButton(): Modifier {
    return this.fillMaxWidth().padding(16.dp, 8.dp, 16.dp, 0.dp)
}

fun Modifier.alertDialog(): Modifier {
    return this.wrapContentWidth().wrapContentHeight()
}

fun Modifier.basicButton(): Modifier {
    return this.fillMaxWidth().padding(16.dp, 8.dp)
}

fun Modifier.fieldModifier(): Modifier {
    return this.fillMaxWidth().padding(16.dp, 4.dp)
}