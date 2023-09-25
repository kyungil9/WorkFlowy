package com.beank.workFlowy.utils

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch


@Composable
fun animateTargetFloatAsState(
    initialValue : Float,
    targetValue: Float,
    animationSpec: AnimationSpec<Float>,
    visibilityThreshold: Float = 0.01f,
    label: String = "FloatAnimation"
): State<Float> {
    var targetting by remember { mutableFloatStateOf(0f) }
    val toolingOverride = remember { mutableStateOf<State<Float>?>(null) }
    val animatable = remember { Animatable(targetting, Float.VectorConverter, visibilityThreshold, label) }
    val animSpec: AnimationSpec<Float> by rememberUpdatedState(
        animationSpec.run {
            if (this is SpringSpec && this.visibilityThreshold != visibilityThreshold) {
                spring(dampingRatio, stiffness, visibilityThreshold)
            } else {
                this
            }
        }
    )
    val channel = remember { Channel<Float>(Channel.CONFLATED) }
    SideEffect {
        channel.trySend(targetValue)
    }
    LaunchedEffect(channel) {
        for (target in channel) {
            val trigger = channel.tryReceive().getOrNull() ?: target
            launch {
                if (trigger == initialValue) {
                    animatable.animateTo(trigger, animSpec)
                }else {
                    animatable.animateTo(trigger, tween(0))
                }
            }
        }
    }
    return toolingOverride.value ?: animatable.asState()
}