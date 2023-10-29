package com.beank.app.utils

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.beank.app.WorkFlowyActivity
import com.beank.app.service.RecordMessageReceiver
import com.beank.workFlowy.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun BroadcastReceiver.goAsync(
    context : CoroutineContext = EmptyCoroutineContext,
    block : suspend CoroutineScope.() -> Unit
){
    val pendingResult = goAsync()
    GlobalScope.launch(context) {
        try {
            block()
        }finally {
            pendingResult.finish()
        }
    }
}

fun notificationBuilder(context: Context, title : String?, body : String?) : NotificationCompat.Builder {
    val intent = Intent(context, WorkFlowyActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    val channelId = context.getString(R.string.default_notification_channel_id)

    return NotificationCompat.Builder(context, channelId)
        .setContentTitle(title)
        .setContentText(body)
        .setSmallIcon(R.mipmap.workflowy)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
}

